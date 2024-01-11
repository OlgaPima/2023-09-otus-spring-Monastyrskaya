package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public Genre insert(String name) {
        return save(name);
    }

    @Override
    public Genre update(String id, String name) {
        return save(id, name);
    }

    @Override
    public void deleteById(String id) {
        // Проверка, что жанр существует:
        genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Errors.GENRE_NOT_FOUND.getMessage().formatted(id)));
        // Проверка, что к нему не привязано ни одной книги:
        if (bookRepository.findByGenreId(id).size() > 0) {
            throw new HasChildEntitiesException("Удаление невозможно: к жанру привязаны книги. Сначала нужно удалить книги этого жанра");
        }
        genreRepository.deleteById(id);
    }

    private Genre save(String name) {
        return save(null, name);
    }

    private Genre save(String id, String name) {
        Genre genre;
        if (id == null || id.isBlank()) {
            genre = new Genre(name);
        }
        else {
            genre = genreRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(Errors.GENRE_NOT_FOUND.getMessage().formatted(id)));
            genre.setName(name);
        }

        return genreRepository.save(genre);
    }
}
