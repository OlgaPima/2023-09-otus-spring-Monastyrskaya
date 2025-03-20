package ru.otus.hw.pageController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    @GetMapping("/authors")
    public String authorsList() {
        return "authorsList";
    }

    @GetMapping("/authors/edit")
    public String editAuthor() {
        return "authorEdit";
    }
}
