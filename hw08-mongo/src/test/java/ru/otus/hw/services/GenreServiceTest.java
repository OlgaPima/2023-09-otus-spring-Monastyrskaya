package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@ComponentScan({"ru.otus.hw.services"})
@DisplayName("Репозиторий на основе Mongo для работы с жанрами ")
public class GenreServiceTest {

    @Autowired
    private GenreServiceImpl genreService;

    @DisplayName("должен блокировать удаление жанра, если есть книги данного жанра")
    @Test
    public void shouldNotDeleteGenreHavingBooks() {
        String authorId = genreService.findAll().stream().findFirst().get().getId();
        assertThrows(HasChildEntitiesException.class, () -> genreService.deleteById(authorId));
    }

    @DisplayName("должен удалять жанр, в котором нет книг")
    @Test
    public void shouldDeleteGenreWithoutBooks() {
        Genre newGenre = genreService.insert("Научная комедия");
        genreService.deleteById(newGenre.getId());
        Optional<Genre> zombiGenre = genreService.findById(newGenre.getId());
        assertTrue(zombiGenre.isEmpty());
    }
}
