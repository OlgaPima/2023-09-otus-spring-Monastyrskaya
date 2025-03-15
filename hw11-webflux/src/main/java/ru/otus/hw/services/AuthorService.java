package ru.otus.hw.services;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;

public interface AuthorService {
    Mono<Author> findById(String id);
}
