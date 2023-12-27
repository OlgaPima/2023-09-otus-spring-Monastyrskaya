package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.BookComment;

@Component
public class BookCommentConverter {

    public String bookCommentToString(BookComment comment) {
        return "CommentId: %d, bookId: %d, commentText: %s".formatted(
                comment.getId(),
                comment.getBook().getId(),
                comment.getCommentText());
    }
}
