package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Mono<Book> findById(String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Не найдена книга с id=%s".formatted(id)))
                );
    }
}