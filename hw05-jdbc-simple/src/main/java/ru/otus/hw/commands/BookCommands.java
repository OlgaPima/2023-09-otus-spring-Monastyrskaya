package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Errors;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;
    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "fbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Книга с id=%d не найдена".formatted(id));
    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, long genreId) {
        return saveChangesAndLog( () -> {
            var savedBook = bookService.insert(title, authorId, genreId);
            return bookConverter.bookToString(savedBook);
        });
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, long genreId) {
        return saveChangesAndLog( () -> {
            var savedBook = bookService.update(id, title, authorId, genreId);
            return bookConverter.bookToString(savedBook);
        });
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public String deleteBook(long id) {
        return saveChangesAndLog( () -> {
            bookService.deleteById(id);
            return null;
        });
    }

    // типа логируем красиво и универсально
    private String saveChangesAndLog(DMLAction action) {
        try {
            return action.saveChangesToDB();
        } catch (EntityNotFoundException e) {
            return Errors.ENTITY_NOT_FOUND.getMessage().formatted(e.getMessage());
        } catch (Exception e) {
            return Errors.OTHER_ERROR.getMessage().formatted(e.getMessage());
        }
    }
}
