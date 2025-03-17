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
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.dto.BookCommentDto;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.EntitySaveError;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentsRestController {

    private final CommentService commentService;

    private final BookService bookService;

    @GetMapping("/api/v1/books/comments")
    public List<BookCommentDto> getCommentsForBook(@RequestParam("bookId") Long bookId) {
        return commentService.findCommentsByBookId(bookId).stream().map(
                comment -> new BookCommentDto(comment.getId().toString(),
                        comment.getCommentText(), BookDto.fromDomainObject(comment.getBook()))
        ).collect(Collectors.toList());
    }

    @PostMapping(value = "/api/v1/books/comments", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public EntitySaveResult<BookCommentDto> saveComment(@RequestParam("bookId") Long bookId,
                                                        @Valid @RequestBody BookCommentDto commentDto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем на клиента ошибки валидации
        }

        BookComment savedComment;
        if (commentDto.getId() == null || commentDto.getId().isBlank()) {
            savedComment = commentService.insert(commentDto.getCommentText(), bookId);
        } else {
            savedComment = commentService.update(Long.parseLong(commentDto.getId()), commentDto.getCommentText());
        }
        return new EntitySaveResult<>(SaveResults.SUCCESS.getName(),
                BookCommentDto.fromDomainObject(savedComment), null);
    }


    @DeleteMapping("/api/v1/books/comments/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
    }

    @ExceptionHandler(Exception.class)
    public EntitySaveResult<BookCommentDto> cannotSaveComment(Exception ex) {
        return new EntitySaveResult<>(SaveResults.ERROR.getName(), null,
                List.of(new EntitySaveError(null, ex.getMessage())));
    }
}
