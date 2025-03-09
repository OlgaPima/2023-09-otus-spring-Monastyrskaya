package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.services.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentsRestController {

    private final CommentService commentService;

    @DeleteMapping("/books/comments/delete")
    public void deleteComment(@RequestParam("commentId") String commentId/*, @RequestParam("bookId") String bookId*/) {
        commentService.delete(commentId);
    }
}
