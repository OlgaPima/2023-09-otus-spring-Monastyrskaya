package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.BookComment;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
    BookComment save(BookComment comment);

    List<BookComment> findByBookId(Long bookId);

    void deleteAllByBookId(Long id);
}
