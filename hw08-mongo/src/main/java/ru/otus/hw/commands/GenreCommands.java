package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.common.Errors;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands extends SaveToDbCommands {

    private final GenreService genreService;
    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find genre by id", key = "fgid")
    public String findGenreById(String id) {
        return genreService.findById(id)
                .map(genreConverter::genreToString)
                .orElse(Errors.GENRE_NOT_FOUND.getMessage().formatted(id));
    }

    @ShellMethod(value = "Insert genre", key = "gins")
    public String insertGenre(String name) {
        return saveChangesAndLog( () -> {
                    var savedBook = genreService.insert(name);
                    return genreConverter.genreToString(savedBook);
                }
        );
    }

    @ShellMethod(value = "Update genre", key = "gupd")
    public String updateGenre(String id, String name) {
        return saveChangesAndLog( () -> {
                    var savedGenre = genreService.update(id, name);
                    return genreConverter.genreToString(savedGenre);
                }
        );
    }

    @ShellMethod(value = "Delete genre by id", key = "gdel")
    public String deleteGenre(String id) {
        return saveChangesAndLog( () -> {
                    genreService.deleteById(id);
                    return "Жанр с id=%s удален".formatted(id);
                }
        );
    }
}
