package ru.otus.hw.services;

import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre findById(Long id);

    GenreDto save(GenreDto genreDto);

    void deleteById(Long id);
}
