package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ComponentScan({"ru.otus.hw.services"})
@DisplayName("Репозиторий на основе Mongo для работы с книгами ")
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookCommentRepository commentRepository;

    @DisplayName("должен сохранять новую книгу с автором и жанром")
    @Test
    public void shouldSaveNewBook() {
        var author = authorRepository.findAll().get(0);
        var genre = genreRepository.findAll().get(0);
        String testBookName = "TestBook";

        Book testBook = bookService.insert(testBookName, author.getId(), genre.getId());
        assertThat(testBook.getId()).isNotNull().isNotBlank();

        var actualBook = bookRepository.findById(testBook.getId());
        assertThat(actualBook).isPresent().get().matches(b -> b.getTitle().equals(testBookName))
                .matches(b -> b.getAuthor() != null && b.getAuthor().getFullName().equals(author.getFullName()))
                .matches(b -> b.getGenre() != null && b.getGenre().getName().equals(genre.getName()));
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

    @DisplayName("должен удалять книгу по id")
    @Test
    public void shouldDeleteBook() {
        String testBookId = bookService.findAll().get(0).getId();
        var testBook = bookRepository.findById(testBookId);
        assertThat(testBook).isNotNull();

        bookService.deleteById(testBookId);
        var deletedBook = bookRepository.findById(testBookId);
       assertThat(deletedBook).isNotPresent();
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    public void shouldDeleteBookWithCommentsWhenBookDeleted() {
        String bookId = commentRepository.findTopByOrderByIdAsc().getBook().getId();
        assertThat(bookRepository.findById(bookId)).isNotNull();

        bookService.deleteById(bookId);
        assertThat(bookRepository.findById(bookId)).isNotPresent();
        assertThat(commentRepository.findByBookId(bookId).size()).isEqualTo(0);
    }
}