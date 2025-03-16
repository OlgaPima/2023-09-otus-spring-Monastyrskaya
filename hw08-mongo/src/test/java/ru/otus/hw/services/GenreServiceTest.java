package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@ComponentScan({"ru.otus.hw.services"})
@DisplayName("Сервис на основе Mongo для работы с жанрами ")
public class GenreServiceTest {

    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("должен сохранять новую книгу")
    public void shouldSaveNewGenre() {
        var newGenre = genreService.insert("Genre1");
        assertThat(newGenre.getId()).isNotNull().isNotBlank();
        assertEquals("Genre1", newGenre.getName());
    }

    @Test
    @DisplayName("должен сохранять измененный жанр")
    public void shouldSaveUpdatedGenre() {
        var testedGenre = genreService.findAll().get(0);
        String newName = "Updated name";

        genreService.update(testedGenre.getId(), newName);
        var updatedGenre = genreRepository.findById(testedGenre.getId());

        assertThat(updatedGenre).isPresent();
        assertThat(updatedGenre.get().getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("должен блокировать удаление жанра, если есть книги данного жанра")
    public void shouldNotDeleteGenreHavingBooks() {
        String authorId = genreService.findAll().stream().findFirst().get().getId();
        assertThrows(HasChildEntitiesException.class, () -> genreService.deleteById(authorId));
    }

    @Test
    @DisplayName("должен удалять жанр, в котором нет книг")
    public void shouldDeleteGenreWithoutBooks() {
        Genre newGenre = genreService.insert("Научная комедия");
        genreService.deleteById(newGenre.getId());
        Optional<Genre> zombiGenre = genreService.findById(newGenre.getId());
        assertTrue(zombiGenre.isEmpty());
    }
}
