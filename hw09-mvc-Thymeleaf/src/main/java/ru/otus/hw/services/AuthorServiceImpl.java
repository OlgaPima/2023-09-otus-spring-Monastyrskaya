package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Errors;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(String id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(id)));
    }

    @Override
    public void deleteById(String id) {
        // Проверка, что автор существует:
        findById(id);
        // Проверка, что к нему не привязано ни одной книги:
        if (bookRepository.existsByAuthorId(id)) {
            throw new HasChildEntitiesException("Удаление невозможно: к автору привязаны книги. " +
                    "Сначала нужно удалить книги этого автора");
        }
        authorRepository.deleteById(id);
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
