package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.models.Errors;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands extends SaveToDbCommands {

    private final AuthorService authorService;
    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find author by id", key = "faid")
    public String findAuthorById(String id) {
        return authorService.findById(id)
                .map(authorConverter::authorToString)
                .orElse(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(id));
    }

    @ShellMethod(value = "Insert author", key = "ains")
    public String insertAuthor(String fullName) {
        return saveChangesAndLog( () -> {
                    var savedBook = authorService.insert(fullName);
                    return authorConverter.authorToString(savedBook);
                }
        );
    }

    @ShellMethod(value = "Update author", key = "aupd")
    public String updateAuthor(String id, String fullName) {
        return saveChangesAndLog( () -> {
                    var savedAuthor = authorService.update(id, fullName);
                    return authorConverter.authorToString(savedAuthor);
                }
        );
    }

    @ShellMethod(value = "Delete book by id", key = "adel")
    public String deleteAuthor(String id) {
        return saveChangesAndLog( () -> {
                    authorService.deleteById(id);
                    return "Автор с id=%s удален".formatted(id);
                }
        );
    }
}
