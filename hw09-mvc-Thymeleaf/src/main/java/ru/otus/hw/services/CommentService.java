package ru.otus.hw.services;

import ru.otus.hw.models.BookComment;

import java.util.List;

public interface CommentService {
    BookComment findById(String id);

    List<BookComment> findCommentsByBookId(String bookId);

    BookComment insert(String comment, String bookId);

    BookComment update(String commentId, String comment);

    BookComment delete(String id);
}
