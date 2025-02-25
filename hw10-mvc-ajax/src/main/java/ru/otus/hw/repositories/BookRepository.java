package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book save(Book book);
    List<Book> findByGenreId(Long genreId);
    List<Book> findByAuthorId(Long authorId);
}
