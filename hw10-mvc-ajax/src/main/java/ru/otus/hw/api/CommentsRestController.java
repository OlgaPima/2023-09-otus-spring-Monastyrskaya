package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.*;
import ru.otus.hw.models.dto.BookCommentDto;
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
                comment -> new BookCommentDto(comment.getId().toString(), comment.getCommentText(), BookDto.fromDomainObject(comment.getBook()))
        ).collect(Collectors.toList());
    }

    @PostMapping("/api/v1/books/comments")
    public EntitySaveResult<BookCommentDto> saveComment(@RequestParam("bookId") Long bookId,
                                                        @Valid BookCommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем на клиента ошибки валидации
        }

        try {
            BookComment savedComment;
            if (commentDto.getId() == null || commentDto.getId().isBlank()) {
//                savedComment = commentService.insert(commentDto.getCommentText(), Long.parseLong(commentDto.getBook().getId()));
                savedComment = commentService.insert(commentDto.getCommentText(), bookId);
            }
            else {
                savedComment = commentService.update(Long.parseLong(commentDto.getId()), commentDto.getCommentText());
            }
            return new EntitySaveResult<>("success", BookCommentDto.fromDomainObject(savedComment), null);
        }
        catch (Exception e) {
            return new EntitySaveResult<>("error", null, List.of(new EntitySaveError(null, e.getMessage())));
        }
    }


    @DeleteMapping("/api/v1/books/comments/{commentId}")
    public RowDeleteResult deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            commentService.delete(commentId);
            return new RowDeleteResult(true);
        }
        catch (HasChildEntitiesException e) {
            return new RowDeleteResult(false);
        }
    }
}
