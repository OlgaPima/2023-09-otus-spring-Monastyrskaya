package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({OrmBookRepository.class, OrmGenreRepository.class, OrmAuthorRepository.class})
class JdbcBookRepositoryTest {

    @Autowired
    private OrmBookRepository bookRepository;

    @Autowired
    private OrmAuthorRepository authorRepository;

    @Autowired
    private OrmGenreRepository genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Optional<Book> actualBook = bookRepository.findByIdWithDetails(1);
        Book expectedBook = testEntityManager.find(Book.class, 1);
        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        int booksExpectedCount = 3;
        int queriesExpectedCount = 1;

        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        System.out.println("\n-------------------------------------------------------------------------------");
        List<Book> books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(booksExpectedCount)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor() != null && !b.getAuthor().getFullName().equals(""))
                .allMatch(s -> s.getGenre() != null);
        System.out.println("\n-------------------------------------------------------------------------------");

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(queriesExpectedCount);
    }

    @DisplayName("должен сохранять новую книгу с автором и жанром")
    @Test
    void shouldSaveNewBook() {
        var author = authorRepository.findById(1).get();
        var genre = genreRepository.findById(1).get();
        String testBookName = "TestBook";

        var testBook = new Book(null, testBookName, author, genre);
        bookRepository.save(testBook);
        assertThat(testBook.getId()).isNotNull().isGreaterThan(0);

        var actualBook = testEntityManager.find(Book.class, testBook.getId());
        assertThat(actualBook).isNotNull().matches(b -> b.getTitle().equals(testBookName))
                .matches(b -> b.getAuthor() != null && b.getAuthor().getFullName().equals(author.getFullName()))
                .matches(b -> b.getGenre() != null && b.getGenre().getName().equals(genre.getName()));
    }

    @DisplayName("должен сохранять измененную книгу с автором и жанром")
    @Test
    void shouldSaveUpdatedBook() {
        var testBook = testEntityManager.find(Book.class, 1);
        testEntityManager.detach(testBook);

        var author = authorRepository.findById(2).get();
        var genre = genreRepository.findById(2).get();
        String newTitle = "Updated title";

        var newBook = new Book(1L, newTitle, author, genre);
        bookRepository.save(newBook);
        var updatedBook = testEntityManager.find(Book.class, 1);

        assertThat(updatedBook.getTitle()).isEqualTo(newTitle);
        assertThat(updatedBook.getAuthor()).isEqualTo(author);
        assertThat(updatedBook.getGenre()).isEqualTo(genre);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        var testBook = testEntityManager.find(Book.class, 1);
        assertThat(testBook).isNotNull();
        testEntityManager.detach(testBook);

        bookRepository.deleteById(1);
        Book deletedBook = testEntityManager.find(Book.class, 1);

        assertThat(deletedBook).isNull();
    }
}