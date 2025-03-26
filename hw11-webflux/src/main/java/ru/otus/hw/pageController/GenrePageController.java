package ru.otus.hw.pageController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GenrePageController {

    @GetMapping("/genres")
    public String genresList() {
        return "genresList";
    }

    @GetMapping("/genres/edit")
    public String editGenre() {
        return "genreEdit";
    }
}
