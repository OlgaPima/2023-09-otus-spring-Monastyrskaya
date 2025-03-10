package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.dto.AuthorDto;
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
    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(id)));
    }

    @Override
    public void deleteById(Long id) {
        // Проверка, что автор существует (иначе - эксепшн):
        findById(id);
        // Проверка, что к нему не привязано ни одной книги:
        if (bookRepository.findByAuthorId(id).size() > 0) {
            throw new HasChildEntitiesException("Удаление невозможно: к автору привязаны книги. " +
                    "Сначала нужно удалить книги этого автора");
        }
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDto save(AuthorDto authorDto) {
        var author = authorDto.toDomainObject();
        return AuthorDto.fromDomainObject(authorRepository.save(author));
    }
}
