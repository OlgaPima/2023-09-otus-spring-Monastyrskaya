package ru.otus.hw.services;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookService {
    Mono<Book> findById(String id);
}
