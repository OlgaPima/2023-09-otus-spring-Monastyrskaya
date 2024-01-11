package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> findAll();
    Optional<Genre> findById(String id);
    Genre insert(String name);
    Genre update(String id, String name);
    void deleteById(String id);
}
