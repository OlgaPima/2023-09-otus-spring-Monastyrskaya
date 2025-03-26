package ru.otus.hw.services;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.dto.BookCommentDto;

public interface CommentService {
    Mono<BookComment> saveComment(Book book, BookCommentDto commentDto);
}