package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.dto.BookCommentDto;
import ru.otus.hw.repositories.BookCommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final BookCommentRepository commentRepository;

    @Override
    public Mono<BookComment> saveComment(Book book, BookCommentDto commentDto) {
        Mono<BookComment> savedComment;
        String commentId = commentDto.getId();

        if (commentId == null || commentId.isBlank()) {
            commentDto.setBook(book.toDtoObject());
            savedComment = commentRepository.save(commentDto.toDomainObject());
        } else {
            savedComment = commentRepository.findById(commentId)
                    .switchIfEmpty(Mono.error(
                            new EntityNotFoundException("Не найден комментарий с id=%s".formatted(commentId)))
                    )
                    .flatMap(foundComment -> {
                        foundComment.setCommentText(commentDto.getCommentText());
                        foundComment.setBook(book);
                        return commentRepository.save(foundComment);
                    });
        }
        return savedComment;
    }
}
