package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.models.BookComment;

import java.util.List;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {
    BookComment save(BookComment comment);

    List<BookComment> findByBookId(String bookId);

    // настраиваем проекцию, чтобы возвращать только id-шники комментариев, без текста и книги
    @Query(value = "{ 'book.id' : ?0 }", fields = "{ '_id' : 1 }")
    List<BookComment> findCommentIdByBookId(String bookId);

    void deleteAllByBookId(String id);

    BookComment findTopByOrderByIdAsc();
}
