package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Errors;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final BookCommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Override
    public BookComment findById(String commentId) {
        return findComment(commentId);
    }

    @Override
    public List<BookComment> findCommentsByBookId(String bookId) {
        // Проверяем, что такая книга существует:
        findBook(bookId);
        // Достаем комментарии
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public BookComment insert(String commentText, String bookId) {
        var book = findBook(bookId);
        var comment = new BookComment(commentText, book);
        return commentRepository.save(comment);
    }

    @Override
    public BookComment update(String commentId, String commentText) {
        var bookComment = findComment(commentId);
        bookComment.setCommentText(commentText);
        return commentRepository.save(bookComment);
    }

    @Override
    public BookComment delete(String commentId) {
        var bookComment = findComment(commentId);
        commentRepository.deleteById(commentId);
        return bookComment;
    }

    private Book findBook(String bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(bookId))
        );
    }

    private BookComment findComment(String commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(Errors.COMMENT_NOT_FOUND.getMessage().formatted(commentId))
        );
    }
}
