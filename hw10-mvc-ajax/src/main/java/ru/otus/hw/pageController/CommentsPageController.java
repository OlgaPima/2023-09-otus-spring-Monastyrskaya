package ru.otus.hw.pageController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentsPageController {

    @GetMapping("/books/comments")
    public String getCommentsForBook() {
        return "bookComments";
    }

    @PostMapping("/books/comments")
    public String saveComment() {
        return "bookComments";
    }
}
