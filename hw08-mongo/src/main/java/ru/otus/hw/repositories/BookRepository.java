package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    Book save(Book book);
    List<Book> findByGenreId(String genreId);
    List<Book> findByAuthorId(String authorId);
    Integer countByAuthorId(String authorId);
    Integer countByGenreId(String genreId);
}
