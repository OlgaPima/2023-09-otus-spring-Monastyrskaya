package ru.otus.hw.repositories;

import ru.otus.hw.models.BookComment;

import java.util.Optional;

public interface BookCommentRepository {
    Optional<BookComment> findById(long id);

    BookComment insert(BookComment comment);

    BookComment update(BookComment comment);

    void delete(long commentId);
}
