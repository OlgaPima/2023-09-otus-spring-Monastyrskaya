package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;

@DataMongoTest
@ComponentScan({"ru.otus.hw.services"})
@DisplayName("Репозиторий на основе Mongo для работы с книгами ")
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookCommentRepository commentRepository;

    @BeforeEach
    public void tearUp() {
        var authorsList = List.of(new Author("1", "С. Кинг"), new Author("2", "Ф. Купер"));
        given(authorRepository.findAll()).willReturn(authorsList);
        given(authorRepository.findById("1")).willReturn(Optional.ofNullable(authorsList.get(0)));
        given(authorRepository.findById("2")).willReturn(Optional.ofNullable(authorsList.get(1)));

        var genresList = List.of(new Genre("1", "Фантастика"), new Genre("2", "Приключения"));
        given(genreRepository.findAll()).willReturn(genresList);
        given(genreRepository.findById("1")).willReturn(Optional.ofNullable(genresList.get(0)));
        given(genreRepository.findById("2")).willReturn(Optional.ofNullable(genresList.get(1)));

        var booksList = List.of(new Book("1", "Лангольеры", authorsList.get(0), genresList.get(0)),
                new Book("2", "Зверобой", authorsList.get(1), genresList.get(1)));
        given(bookRepository.findAll()).willReturn(booksList);
        given(bookRepository.findById("1")).willReturn(Optional.ofNullable(booksList.get(0)));
        given(bookRepository.findById("2")).willReturn(Optional.ofNullable(booksList.get(1)));
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    public void shouldReturnCorrectBooksList() {

        var books = bookService.findAll();
        assertThat(books).isNotNull().hasSize(2)
                .allMatch(a -> !a.getTitle().isEmpty())
                .allMatch(a -> a.getAuthor() != null)
                .allMatch(a -> a.getGenre()!= null);
    }

    @DisplayName("должен сохранять измененную книгу с автором и жанром")
    @Test
    public void shouldSaveUpdatedBook() {
        var testedBook = bookService.findAll().get(0);
        String newTitle = "Updated title";
        var newAuthor = authorRepository.findAll().stream().filter(
                author -> !author.getId().equals(testedBook.getAuthor().getId())
        ).findFirst().get();
        var newGenre = genreRepository.findAll().stream().filter(
                genre -> !genre.getId().equals(testedBook.getGenre().getId())
        ).findFirst().get();

        bookService.update(testedBook.getId(), newTitle, newAuthor.getId(), newGenre.getId());
        var updatedBook = bookRepository.findById(testedBook.getId());

        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getTitle()).isEqualTo(newTitle);
        assertThat(updatedBook.get().getAuthor()).isEqualTo(newAuthor);
        assertThat(updatedBook.get().getGenre()).isEqualTo(newGenre);
    }
}