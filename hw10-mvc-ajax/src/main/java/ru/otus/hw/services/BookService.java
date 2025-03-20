package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(Long id);

    List<Book> findAll();

    BookDto save(BookDto book);

    void deleteById(Long id);
}
