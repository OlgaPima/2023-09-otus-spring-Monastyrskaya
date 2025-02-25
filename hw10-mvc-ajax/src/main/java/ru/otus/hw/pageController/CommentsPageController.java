package ru.otus.hw.pageController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.dto.BookCommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentsPageController {

    private final CommentService commentService;
//    private final BookService bookService;

    @GetMapping("/books/comments")
    public String getCommentsForBook(@RequestParam(value = "bookId") Long bookId) {
//        var commentDto = new BookCommentDto();
//        model.addAttribute("commentDto", commentDto);
//
//        return fillCommentsListPage(bookId, model);
        return "bookComments";
    }

    @PostMapping("/books/comments")
    public String saveComment() { //@RequestParam("bookId") Long bookId, @Valid @ModelAttribute("commentDto") BookCommentDto commentDto,
//                              BindingResult bindingResult, Model model) {
//        if (!bindingResult.hasErrors()) {
//            commentService.insert(commentDto.getCommentText(), bookId);
//        }
//        return fillCommentsListPage(bookId, model);
        return "bookComments";
    }

    @PostMapping("/books/comments/edit")
    public String editComment(@RequestParam("bookId") Long bookId, @RequestParam("commentId") Long commentId,
                              @Valid @ModelAttribute("commentDto") BookCommentDto commentDto,
                              BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            commentService.update(commentId, commentDto.getCommentText());
        }
        return "redirect:/books/comments?bookId=" + bookId;
    }


//    private String fillCommentsListPage(Long bookId, Model model) {
//        List<BookComment> comments = commentService.findCommentsByBookId(bookId);
//        Book book = comments.size() > 0 ? comments.get(0).getBook() : bookService.findById(bookId).get();
//
//        model.addAttribute("comments", comments);
//        model.addAttribute("book", book);
//        return "bookComments";
//    }

//    @GetMapping("/books/comments/delete")
//    public String deleteComment(@RequestParam("commentId") Long commentId, @RequestParam("bookId") Long bookId) {
//        commentService.delete(commentId);
//        return "redirect:/books/comments?bookId=" + bookId;
//    }
}
