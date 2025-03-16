package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.EntitySaveError;
import ru.otus.hw.models.dto.BookCommentDto;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentsRestController {
    private final BookRepository bookRepository;

    private final CommentServiceImpl commentService;

    private final BookCommentRepository commentRepository;

    @GetMapping("/api/v1/books/comments")
    public Flux<BookCommentDto> getCommentsForBook(@RequestParam("bookId") String bookId) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Не найдена книга с id=%s".formatted(bookId)))
                )
                .flatMapMany(book -> commentRepository.findByBookId(bookId).map(BookCommentDto::fromDomainObject));
    }

    @PostMapping(value = "/api/v1/books/comments", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public Mono<EntitySaveResult<BookCommentDto>> saveComment(@RequestParam("bookId") String bookId,
                                                              @Valid @RequestBody BookCommentDto commentDto,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Mono.just(new EntitySaveResult<>(bindingResult)); // выкидываем на клиента ошибки валидации
        }

        return bookRepository.findById(bookId)
                    .switchIfEmpty(Mono.error(
                            new EntityNotFoundException("Не найдена книга с id=%s".formatted(bookId)))
                    )
                    .flatMap(book -> commentService.saveComment(book, commentDto))
                    .map(BookCommentDto::fromDomainObject)
                    .map(dtoComment -> new EntitySaveResult<>(SaveResults.SUCCESS.getName(), dtoComment, null))
                    .doOnError(ex -> System.out.println("Ошибка сохранения: " + ex.getMessage()))
                    .onErrorResume(ex -> Mono.just(
                            new EntitySaveResult<>(SaveResults.ERROR.getName(), null,
                                    List.of(new EntitySaveError(null, ex.getMessage())))
                    ));
    }

    @DeleteMapping("/api/v1/books/comments/{commentId}")
    public Mono<Void> deleteComment(@PathVariable("commentId") String commentId) {
        return commentRepository.deleteById(commentId);
    }
}
