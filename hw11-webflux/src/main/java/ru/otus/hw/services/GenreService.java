package ru.otus.hw.services;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Genre;

public interface GenreService {
     Mono<Genre> findById(String id);
}
