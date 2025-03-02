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
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private boolean redirectFromDelete;
    private String genreIdChildEntitiesError;

    @GetMapping("/genres")
    public String genresList(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);

        if (redirectFromDelete) {
            // При следующей загрузке страницы надо скрыть сообщение об ошибке "Есть книги данного жанра, удаление невозможно"
            // Иначе оно там залипает и остается навечно, при любом переходе между страницами.
            // Сбрасываем флаг для этого  - сообщение показывается однократно, затем исчезает
            model.addAttribute("genreIdChildEntitiesError", genreIdChildEntitiesError);
            redirectFromDelete = false;
        }
        else {
            model.addAttribute("genreIdChildEntitiesError", null);
        }

        return "genresList";
    }

    @GetMapping("/genres/edit")
    public String editGenre(@RequestParam(required = false) String id, Model model) {
        Genre genre;
        if (id == null || id.isBlank()) {
            genre = new Genre();
        }
        else {
            genre = genreService.findById(id);
        }
        model.addAttribute("genre", genre);

        return "genreEdit";
    }

    @PostMapping("/genres/edit")
    public String saveGenre(@Valid @ModelAttribute("genre") GenreDto genreDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "genreEdit";
        }
        genreService.save(genreDto.toDomainObject());
        return "redirect:/genres";
    }

    @GetMapping("/genres/delete")
    public String deleteGenre(@RequestParam("id") String id) {
        try {  // удаление жанра - проверяем, есть ли книги, если есть - блокируем удаление и выводим ошибку:
            genreService.deleteById(id);
            return "redirect:/genres";
        }
        catch (HasChildEntitiesException e) {
            genreIdChildEntitiesError = id;
            redirectFromDelete = true;
            return "redirect:/genres";
        }
    }

}
