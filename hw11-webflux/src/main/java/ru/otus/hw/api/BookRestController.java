package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.*;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookCommentRepository commentRepository;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> booksList() {
        return bookRepository.findAll().map(BookDto::fromDomainObject);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBookById(@PathVariable(required = false) String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Не найдена книга с id=%s".formatted(id)))
                ).map(BookDto::fromDomainObject);
    }

    @PostMapping(value = "/api/v1/books", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public Mono<EntitySaveResult<BookDto>> saveBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Mono.just(new EntitySaveResult<>(bindingResult)); // выкидываем ошибки валидации на клиента
        }

        String authorId = bookDto.getAuthor().getId();
        String genreId = bookDto.getGenre().getId();

        Mono<Boolean> authorMono = authorRepository.existsById(authorId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не найден автор с id=%s".formatted(authorId))));

        Mono<Boolean> genreMono = genreRepository.existsById(genreId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не найден жанр с id=%s".formatted(genreId))));

        return Mono.zip(authorMono, genreMono)
                .flatMap(tuple -> {
                    Book book = bookDto.toDomainObject();
                    return bookRepository.save(book)
                            .map(BookDto::fromDomainObject)
                            .map(savedBook -> new EntitySaveResult<>(SaveResults.SUCCESS.getName(), savedBook, null))
                            .onErrorResume(ex ->
                                    Mono.just(new EntitySaveResult<>(SaveResults.ERROR.getName(), null, null))
                            );
                });
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Boolean> deleteBook(@PathVariable("id") String id) {
        return commentRepository.deleteAllByBookId(id).then(bookRepository.deleteById(id)).thenReturn(true);
    }
}
