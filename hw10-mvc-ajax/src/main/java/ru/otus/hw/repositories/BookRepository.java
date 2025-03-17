package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book save(Book book);

    boolean existsByGenreId(Long genreId);

    boolean existsByAuthorId(Long authorId);
}
