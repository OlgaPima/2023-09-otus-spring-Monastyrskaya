package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final BookCommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Override
    public BookComment findById(long commentId) {
        return findComment(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookComment> findCommentsByBookId(long bookId) {
        var book = findBook(bookId);
        List<BookComment> comments = book.getComments();
        Hibernate.initialize(comments);
        return comments;
    }

    @Override
    public BookComment insert(String commentText, long bookId) {
        var book = findBook(bookId);
        var comment = new BookComment(null, commentText, book);
        return commentRepository.save(comment);
    }

    @Override
    public BookComment update(long commentId, String commentText) {
        var bookComment = findComment(commentId);
        bookComment.setCommentText(commentText);
        return commentRepository.save(bookComment);
    }

    @Override
    public BookComment delete(long commentId) {
        var bookComment = findComment(commentId);
        commentRepository.deleteById(commentId);
        return bookComment;
    }

    private Book findBook(long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Не найдена книга с id=%d".formatted(bookId))
        );
    }

    private BookComment findComment(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("Не найден комментарий с id=%d".formatted(commentId))
        );
    }
}
