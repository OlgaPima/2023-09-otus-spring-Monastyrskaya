package ru.otus.hw.pageController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    @GetMapping("/books")
    public String booksList() {
        return "booksList";
    }

    @GetMapping("/books/edit")
    public String editBook() {
        return "bookEdit";
    }
}