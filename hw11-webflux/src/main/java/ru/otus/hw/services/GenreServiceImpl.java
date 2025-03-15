package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Mono<Genre> findById(String id) {
        return genreRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Не найден жанр с id=%s".formatted(id))));
    }
}