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
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> genresList() {
        return genreRepository.findAll().map(GenreDto::fromDomainObject);
    }

    @GetMapping("/api/v1/genres/{id}")
    public Mono<GenreDto> getGenreById(@PathVariable(required = false) String id) {
        return genreRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Не найден жанр с id=%s".formatted(id))))
                .map(GenreDto::fromDomainObject);
    }

    @PostMapping(value = "/api/v1/genres", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public Mono<EntitySaveResult<GenreDto>> saveGenre(@Valid @RequestBody GenreDto genreDto,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Mono.just(new EntitySaveResult<>(bindingResult)); // выкидываем ошибки валидации на клиента
        }

        var genre = genreDto.toDomainObject();
        return genreRepository.save(genre).map(GenreDto::fromDomainObject)
                .map(savedGenre -> new EntitySaveResult<>(SaveResults.SUCCESS.getName(), savedGenre, null));
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public Mono<Boolean> deleteGenre(@PathVariable("id") String id) {
        // проверяем, есть ли книги данного жанра, если есть - блокируем удаление
        return bookRepository.existsByGenreId(id)
                .flatMap(hasBooks -> {
                    if (hasBooks) {
                        return Mono.just(false);
                    } else {
                        return genreRepository.deleteById(id).thenReturn(true);
                    }
                });
    }
}
