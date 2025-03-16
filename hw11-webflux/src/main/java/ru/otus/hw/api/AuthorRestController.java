package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @GetMapping(value = "/api/v1/authors")
    public Flux<AuthorDto> authorsList() {
        return authorRepository.findAll().map(AuthorDto::fromDomainObject);
    }

    @GetMapping(value = "/api/v1/authors/{id}")
    public Mono<AuthorDto> getAuthorById(@PathVariable(required = false) String id) {
        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Не найден автор с id=%s".formatted(id)))
                ).map(AuthorDto::fromDomainObject);
    }

    @PostMapping(value = "/api/v1/authors", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public Mono<EntitySaveResult<AuthorDto>> saveAuthor(@Valid @RequestBody AuthorDto authorDto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Mono.just(new EntitySaveResult<>(bindingResult)); // выкидываем ошибки валидации на клиента
        }

        var author = authorDto.toDomainObject();
        return authorRepository.save(author).map(AuthorDto::fromDomainObject)
                .map(savedAuthor -> new EntitySaveResult<>(SaveResults.SUCCESS.getName(), savedAuthor, null));
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public Mono<Boolean> deleteAuthor(@PathVariable("id") String id) {
        // проверяем, есть ли книги данного автора, если есть - блокируем удаление
        return bookRepository.existsByAuthorId(id)
                .flatMap(hasBooks -> {
                    if (hasBooks) {
                        return Mono.just(false);
                    } else {
                        return authorRepository.deleteById(id).thenReturn(true);
                    }
                });
    }
}
