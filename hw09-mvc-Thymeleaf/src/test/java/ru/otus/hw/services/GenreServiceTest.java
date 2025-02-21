//package ru.otus.hw.services;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.context.annotation.ComponentScan;
//import ru.otus.hw.exceptions.HasChildEntitiesException;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@DataMongoTest
//@ComponentScan({"ru.otus.hw.services"})
//@DisplayName("Репозиторий на основе Mongo для работы с жанрами ")
//public class GenreServiceTest {
//
//    @Autowired
//    private GenreServiceImpl genreService;
//
//    @DisplayName("должен блокировать удаление жанра, если есть книги данного жанра")
//    @Test
//    public void shouldNotDeleteGenreHavingBooks() {
//        String authorId = genreService.findAll().stream().findFirst().get().getId();
//        assertThrows(HasChildEntitiesException.class, () -> genreService.deleteById(authorId));
//    }
//}
