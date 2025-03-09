package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public String genresLis(@RequestParam(name = "cannotDelId", required = false)
                                        String cannotDelGenreId, Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        model.addAttribute("cannotDelId", cannotDelGenreId);

        return "genresList";
    }

    @GetMapping("/genres/edit")
    public String editGenre(@RequestParam(required = false) String id, Model model) {
        Genre genre;
        if (id == null || id.isBlank()) {
            genre = new Genre();
        } else {
            genre = genreService.findById(id);
        }
        model.addAttribute("genre", genre);

        return "genreEdit";
    }

    @PostMapping("/genres/edit")
    public String saveGenre(@Valid @ModelAttribute("genre") GenreDto genreDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "genreEdit";
        }
        genreService.save(genreDto.toDomainObject());
        return "redirect:/genres";
    }
}
