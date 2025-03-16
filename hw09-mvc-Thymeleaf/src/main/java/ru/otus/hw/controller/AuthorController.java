package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String authorsList(@RequestParam(name = "cannotDelId", required = false)
                                          String cannotDelAuthorId, Model model) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        // Этот параметр нужен для отображения в гриде сообщения об ошибке
        // "Есть книги данного автора, удаление невозможно"
        model.addAttribute("cannotDelId", cannotDelAuthorId);

        return "authorsList";
    }

    @GetMapping("/authors/edit")
    public String editAuthor(@RequestParam(required = false) String id, Model model) {
        Author author;
        if (id == null || id.isBlank()) {
            author = new Author();
        } else {
            author = authorService.findById(id);
        }
        model.addAttribute("author", author);
        return "authorEdit";
    }

    @PostMapping("/authors/edit")
    public String saveAuthor(@Valid @ModelAttribute("author") AuthorDto authorDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authorEdit";
        }
        authorService.save(authorDto.toDomainObject());
        return "redirect:/authors";
    }

    @DeleteMapping("/authors/delete")
    public String deleteAuthor(@RequestParam("id") String id) {
        // удаление автора - проверяем, есть ли книги, если есть - блокируем удаление и выводим на клиенте ошибку:
        authorService.deleteById(id);
        return "redirect:/authors";
    }

    @ExceptionHandler(HasChildEntitiesException.class)
    public String cannotDelAuthor(HasChildEntitiesException ex) {
        return "redirect:/authors?cannotDelId=" + ex.getDeletingId();
    }
}
