package ru.otus.hw.services;

import ru.otus.hw.models.BookComment;

import java.util.List;

public interface CommentService {
    BookComment findById(Long id);
    List<BookComment> findCommentsByBookId(Long bookId);
    BookComment insert(String comment, Long bookId);
    BookComment update(Long commentId, String comment);
    BookComment delete(Long id);
}
