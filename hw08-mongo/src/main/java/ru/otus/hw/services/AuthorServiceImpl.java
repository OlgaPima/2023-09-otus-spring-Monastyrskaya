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
import java.util.Optional;

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
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public Author insert(String fullName) {
        return save(fullName);
    }

    @Override
    public Author update(String id, String fullName) {
        return save(id, fullName);
    }

    @Override
    public void deleteById(String id) {
        // Проверка, что автор существует:
        authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(id)));
        // Проверка, что к нему не привязано ни одной книги:
        if (bookRepository.findByAuthorId(id).size() > 0) {
            throw new HasChildEntitiesException("Удаление невозможно: к автору привязаны книги. Сначала нужно удалить книги этого автора");
        }
        authorRepository.deleteById(id);
    }

    private Author save(String fullName) {
        return save(null, fullName);
    }

    private Author save(String id, String fullName) {
        Author author;
        if (id == null || id.isBlank()) {
            author = new Author(fullName);
        }
        else {
            author = authorRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(id)));
            author.setFullName(fullName);
        }

        return authorRepository.save(author);
    }
}
