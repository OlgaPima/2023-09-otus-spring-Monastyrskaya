package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Mono<Author> findById(String id) {
        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Не найден автор с id=%s".formatted(id))));
    }
}