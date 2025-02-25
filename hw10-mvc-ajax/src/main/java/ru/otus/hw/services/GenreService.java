package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
    Genre findById(Long id);
    Genre save(Genre genre);
    void deleteById(Long id);
}
