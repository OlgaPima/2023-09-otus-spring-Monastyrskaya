package ru.otus.hw.services;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    Author findById(Long id);

    AuthorDto save(AuthorDto authorDto);

    void deleteById(Long id);
}
