package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookCommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommand extends AbstractSaveCommands {
    private final CommentService commentService;

    private final BookCommentConverter commentConverter;

    @ShellMethod(value = "Find all comments for book by bookId", key = "fbc")
    public String findBookComments(String bookId) {
        return commentService.findCommentsByBookId(bookId).stream()
                .map(commentConverter::bookCommentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment for book", key = "cins")
    public String insertComment(String comment, String bookId) {
        return saveChangesAndLog(() -> {
                var savedComment = commentService.insert(comment, bookId);
                return commentConverter.bookCommentToString(savedComment);
            }
        );
    }

    @ShellMethod(value = "Update comment for book by commentId", key = "cupd")
    public String updateComment(String commentId, String commentText) {
        return saveChangesAndLog(() -> {
                var savedComment = commentService.update(commentId, commentText);
                return commentConverter.bookCommentToString(savedComment);
            }
        );
    }

    @ShellMethod(value = "Delete comment for book by commentId", key = "cdel")
    public String deleteComment(String commentId) {
        return saveChangesAndLog(() -> {
                commentService.delete(commentId);
                return "Комментарий с id=%s удален".formatted(commentId);
            }
        );
    }
}
