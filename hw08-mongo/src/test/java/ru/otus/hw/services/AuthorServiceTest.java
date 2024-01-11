package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.exceptions.HasChildEntitiesException;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ComponentScan({"ru.otus.hw.services"})
@DisplayName("Репозиторий на основе Mongo для работы с авторами ")
public class AuthorServiceTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @DisplayName("должен блокировать удаление автора, если у него есть книги")
    @Test
    public void shouldNotDeleteAuthorHavingBooks() {
        String authorId = authorService.findAll().stream().findFirst().get().getId();
        assertThrows(HasChildEntitiesException.class, () -> authorService.deleteById(authorId));
    }
}
