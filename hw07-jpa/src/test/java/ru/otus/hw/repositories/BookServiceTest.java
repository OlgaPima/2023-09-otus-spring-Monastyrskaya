package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({BookServiceImpl.class})
class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен сохранять новую книгу с автором и жанром")
    @Test
    void shouldSaveNewBook() {
        String testBookName = "TestBook";

        var testBook = bookService.insert(testBookName, 1, 1);
        assertThat(testBook.getId()).isNotNull().isGreaterThan(0);

        var actualBook = testEntityManager.find(Book.class, testBook.getId());
        assertThat(actualBook).isNotNull().matches(b -> b.getTitle().equals(testBookName))
                .matches(b -> b.getAuthor() != null && b.getAuthor().getFullName().equals("Author_1"))
                .matches(b -> b.getGenre() != null && b.getGenre().getName().equals("Genre_1"));
    }

    @DisplayName("должен сохранять измененную книгу с автором и жанром")
    @Test
    void shouldSaveUpdatedBook() {
        String newTitle = "Updated title";
        long newAuthor = 2L;
        long newGenre = 2L;

        bookService.update(1L, newTitle, newAuthor, newGenre);
        var updatedBook = testEntityManager.find(Book.class, 1);

        var author = authorRepository.findById(2L).get();
        var genre = genreRepository.findById(2L).get();

        assertThat(updatedBook.getTitle()).isEqualTo(newTitle);
        assertThat(updatedBook.getAuthor()).isEqualTo(author);
        assertThat(updatedBook.getGenre()).isEqualTo(genre);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        var testBook = testEntityManager.find(Book.class, 1);
        assertThat(testBook).isNotNull();

        bookService.deleteById(1);
        Book deletedBook = testEntityManager.find(Book.class, 1);

        assertThat(deletedBook).isNull();
    }
}