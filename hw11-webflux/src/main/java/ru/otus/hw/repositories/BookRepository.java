package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> save(Book book);

    Mono<Boolean> existsByGenreId(String genreId);

    Mono<Boolean> existsByAuthorId(String authorId);
}
