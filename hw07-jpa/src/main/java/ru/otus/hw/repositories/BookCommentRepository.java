package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.BookComment;

import java.util.Optional;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
    Optional<BookComment> findById(long id);

    BookComment save(BookComment comment);

    void deleteById(long commentId);
}
