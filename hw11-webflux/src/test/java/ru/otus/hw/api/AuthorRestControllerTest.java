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
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@WebFluxTest
@ContextConfiguration(classes = {AuthorRestController.class})
@DisplayName("AuthorRestController: ")
public class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    private final List<AuthorDto> authorsDtoList = List.of(
            new AuthorDto("1", "С. Кинг", 1947),
            new AuthorDto("2", "Ф. Купер", 1789)
    );

    @Test
    @DisplayName("должен возвращать список всех авторов")
    public void shouldReturnAllAuthorsList() {
        when(authorRepository.findAll()).thenReturn(Flux.fromStream(
                authorsDtoList.stream().map(AuthorDto::toDomainObject)));

        webTestClient.get().uri("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(2)
                .isEqualTo(authorsDtoList);
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("должен находить автора по id")
    public void shouldFindAuthorById() {
        when(authorRepository.findById(authorsDtoList.get(0).getId()))
                .thenReturn(Mono.just(authorsDtoList.get(0).toDomainObject()));

        webTestClient.get().uri("/api/v1/authors/{id}", authorsDtoList.get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AuthorDto.class)
                .isEqualTo(authorsDtoList.get(0));

        verify(authorRepository, times(1)).findById(authorsDtoList.get(0).getId());
    }

    @Test
    @DisplayName("должен выдавать ошибку, если автор не найден")
    void shouldReturnErrorWhenAuthorNotFound() {
        String authorId = "badId";
        when(authorRepository.findById(authorId)).thenReturn(Mono.error(new EntityNotFoundException("")));

        webTestClient.get().uri("/api/v1/authors/{id}", authorId)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    @DisplayName("должен удалять автора")
    public void shouldDeleteAuthor() {
        var testedAuthorId = authorsDtoList.get(1).getId();
        when(bookRepository.existsByAuthorId(testedAuthorId)).thenReturn(Mono.just(false));
        when(authorRepository.deleteById(testedAuthorId)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/authors/{id}", testedAuthorId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        verify(authorRepository,times(1)).deleteById(testedAuthorId);
    }
}
