package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.BookComment;

import java.util.List;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {
    BookComment save(BookComment comment);

    List<BookComment> findByBookId(String bookId);

    BookComment findTopByOrderByIdAsc();
}
