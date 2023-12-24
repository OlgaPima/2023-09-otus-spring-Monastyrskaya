package ru.otus.hw.services;

import ru.otus.hw.models.BookComment;

import java.util.List;

public interface CommentService {
    BookComment findById(long id);

    List<BookComment> findCommentsByBookId(long bookId);

    BookComment insert(String comment, long bookId);

    BookComment update(long commentId, String comment);

    BookComment delete(long id);
}
