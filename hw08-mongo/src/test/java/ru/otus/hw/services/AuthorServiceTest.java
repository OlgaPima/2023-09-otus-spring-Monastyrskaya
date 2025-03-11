package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@ComponentScan({"ru.otus.hw.services"})
@DisplayName("Сервис на основе Mongo для работы с авторами ")
public class AuthorServiceTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("должен сохранять новую книгу")
    public void shouldSaveNewGenre() {
        var newAuthor = authorService.insert("Author1");
        assertThat(newAuthor.getId()).isNotNull().isNotBlank();
        assertEquals("Author1", newAuthor.getFullName());
    }

    @Test
    @DisplayName("должен сохранять измененного автора")
    public void shouldSaveUpdatedGenre() {
        var testedAuthor = authorService.findAll().get(0);
        String newName = "Updated fullname";

        authorService.update(testedAuthor.getId(), newName);
        var updatedAuthor = authorRepository.findById(testedAuthor.getId());

        assertThat(updatedAuthor).isPresent();
        assertThat(updatedAuthor.get().getFullName()).isEqualTo(newName);
    }

    @DisplayName("должен блокировать удаление автора, если у него есть книги")
    @Test
    public void shouldNotDeleteAuthorHavingBooks() {
        String authorId = authorService.findAll().stream().findFirst().get().getId();
        assertThrows(HasChildEntitiesException.class, () -> authorService.deleteById(authorId));
    }

    @DisplayName("должен удалять автора, у которого нет книг")
    @Test
    public void shouldDeleteAuthorWithoutBooks() {
        Author newAuthor = authorService.insert("ЧукчаНеПисатель");
        authorService.deleteById(newAuthor.getId());
        Optional<Author> zombiAuthor = authorService.findById(newAuthor.getId());
        assertTrue(zombiAuthor.isEmpty());
    }
}
