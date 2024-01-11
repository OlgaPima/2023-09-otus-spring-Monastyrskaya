package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.BookComment;

@Component
public class BookCommentConverter {

    public String bookCommentToString(BookComment comment) {
        return "CommentId: %s, bookId: %s, commentText: %s".formatted(
                comment.getId(),
                comment.getBook().getId(),
                comment.getCommentText());
    }
}
