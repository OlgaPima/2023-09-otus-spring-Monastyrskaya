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
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final AuthorServiceImpl authorService;

    private final GenreService genreService;

    private final BookService bookService;

    private final BookRepository bookRepository;

    private final BookCommentRepository commentRepository;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> booksList() {
        return bookRepository.findAll().map(BookDto::fromDomainObject);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBookById(@PathVariable(required = false) String id) {
        return bookService.findById(id).map(BookDto::fromDomainObject);
    }

    @PostMapping(value = "/api/v1/books", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public Mono<EntitySaveResult<BookDto>> saveBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Mono.just(new EntitySaveResult<>(bindingResult)); // выкидываем ошибки валидации на клиента
        }

        return authorService.findById(bookDto.getAuthor().getId())
                .then(genreService.findById(bookDto.getGenre().getId()))
                .then(bookRepository.save(bookDto.toDomainObject())
                        .map(BookDto::fromDomainObject)
                        .map(savedBook -> new EntitySaveResult<>(SaveResults.SUCCESS.getName(), savedBook, null))
                        .onErrorResume(ex -> {
                            System.out.println("Ошибка сохранения книги: " + ex.getMessage());
                            return Mono.just(new EntitySaveResult<>(SaveResults.ERROR.getName(), null, null));
                        })
                );
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Boolean> deleteBook(@PathVariable("id") String id) {
        return commentRepository.deleteAllByBookId(id).then(bookRepository.deleteById(id)).thenReturn(true);
    }
}
