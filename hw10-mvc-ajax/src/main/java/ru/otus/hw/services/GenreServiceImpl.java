package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

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
    public Genre findById(Long id) {
        return genreRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Errors.GENRE_NOT_FOUND.getMessage().formatted(id)));
    }

    @Override
    public void deleteById(Long id) {
        // Проверка, что жанр существует:
        findById(id);
        // Проверка, что к нему не привязано ни одной книги:
        if (bookRepository.existsByGenreId(id)) {
            throw new HasChildEntitiesException("Удаление невозможно: к жанру привязаны книги. " +
                    "Сначала нужно удалить книги этого жанра");
        }
        genreRepository.deleteById(id);
    }

    @Override
    public GenreDto save(GenreDto genreDto) {
        var genre = genreDto.toDomainObject();
        return GenreDto.fromDomainObject(genreRepository.save(genre));
    }
}
