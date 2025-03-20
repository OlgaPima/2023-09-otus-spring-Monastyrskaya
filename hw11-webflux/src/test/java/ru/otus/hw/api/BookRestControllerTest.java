package ru.otus.hw.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest
@ContextConfiguration(classes = {BookRestController.class})
@DisplayName("BookController:")
public class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookCommentRepository commentRepository;

    private final List<BookDto> booksDtoList = List.of(
            new BookDto("1", "Book1", new Author("1", "Author1", 1967), new Genre("1", "genre1")),
            new BookDto("2", "Book2", new Author("2", "Author2", 1994), new Genre("2", "genre2"))
    );

    @Test
    @DisplayName("должен возвращать список книг")
    public void shouldReturnCorrectBooksList() {
        when(bookRepository.findAll()).thenReturn(Flux.fromStream(
                booksDtoList.stream().map(BookDto::toDomainObject)));

        webTestClient.get().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .isEqualTo(booksDtoList);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("должен находить книгу по id")
    public void shouldFindAuthorById() {
        var testedBook = booksDtoList.get(0);
        when(bookRepository.findById(testedBook.getId()))
                .thenReturn(Mono.just(booksDtoList.get(0).toDomainObject()));

        webTestClient.get().uri("/api/v1/books/{id}", testedBook.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(testedBook);

        verify(bookRepository, times(1)).findById(testedBook.getId());
    }

    @Test
    @DisplayName("должен выдавать ошибку, если книга не найдена")
    void shouldReturnErrorWhenBookNotFound() {
        String bookId = "badId";
        when(authorRepository.findById(bookId)).thenReturn(Mono.error(new EntityNotFoundException("")));

        webTestClient.get().uri("/api/v1/books/{id}", bookId)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    @DisplayName("должен удалять книгу")
    public void shouldDeleteBook() {
        var testedBookId = booksDtoList.get(0).getId();
        when(bookRepository.existsByAuthorId(testedBookId)).thenReturn(Mono.just(false));
        when(bookRepository.deleteById(testedBookId)).thenReturn(Mono.empty());
        when(commentRepository.deleteAllByBookId(testedBookId)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/books/{id}", testedBookId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        verify(bookRepository,times(1)).deleteById(testedBookId);
        verify(commentRepository,times(1)).deleteAllByBookId(testedBookId);
    }
}