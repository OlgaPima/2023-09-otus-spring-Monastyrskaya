package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Errors;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@ShellComponent
@RequiredArgsConstructor
public class BookCommands extends SaveToDbCommands {

    private final BookService bookService;
    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "fbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse(Errors.BOOK_NOT_FOUND.getMessage().formatted(id));
    }

    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, String genreId) {
        return saveChangesAndLog( () -> {
                var savedBook = bookService.insert(title, authorId, genreId);
                return bookConverter.bookToString(savedBook);
            }
        );
    }

    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, String genreId) {
        return saveChangesAndLog( () -> {
                var savedBook = bookService.update(id, title, authorId, genreId);
                return bookConverter.bookToString(savedBook);
            }
        );
    }

    @ShellMethod(value = "Delete book by id", key = "bdel")
    public String deleteBook(String id) {
        return saveChangesAndLog( () -> {
                bookService.deleteById(id);
                return "Книга с id=%s удалена".formatted(id);
            }
        );
    }
}