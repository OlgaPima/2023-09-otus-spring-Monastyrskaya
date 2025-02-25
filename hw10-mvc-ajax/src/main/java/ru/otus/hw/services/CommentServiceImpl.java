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
    public BookComment findById(Long commentId) {
        return findComment(commentId);
    }

    @Override
    public List<BookComment> findCommentsByBookId(Long bookId) {
        // Проверяем, что такая книга существует:
        findBook(bookId);
        // Достаем комментарии
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public BookComment insert(String commentText, Long bookId) {
        var book = findBook(bookId);
        var comment = new BookComment(commentText, book);
        return commentRepository.save(comment);
    }

    @Override
    public BookComment update(Long commentId, String commentText) {
        var bookComment = findComment(commentId);
        bookComment.setCommentText(commentText);
        return commentRepository.save(bookComment);
    }

    @Override
    public BookComment delete(Long commentId) {
        var bookComment = findComment(commentId);
        commentRepository.deleteById(commentId);
        return bookComment;
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(bookId))
        );
    }

    private BookComment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(Errors.COMMENT_NOT_FOUND.getMessage().formatted(commentId))
        );
    }
}
