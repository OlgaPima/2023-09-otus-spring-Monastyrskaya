package ru.otus.hw.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String authorsList(Model model, HttpSession session) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        boolean redirectFromDelete = session.getAttribute("redirectFromDelete") != null && (boolean)session.getAttribute("redirectFromDelete");
        if (redirectFromDelete) {
            // При следующей загрузке страницы надо скрыть возможное сообщение об ошибке "Есть книги данного автора, удаление невозможно"
            // (которое могло возникнуть там ранее при попытке кого-то удалить) - иначе оно там залипает и остается навечно,
            // при любом переходе между страницами. Сбрасываем флаг для этого - сообщение показывается однократно, затем исчезает
            session.setAttribute("redirectFromDelete", false);
        }
        else {
            session.setAttribute("authorIdChildEntitiesError", null);
        }
        return "authorsList";
    }

    @GetMapping("/authors/edit")
    public String editAuthor(@RequestParam(required = false) String id, Model model) {
        Author author;
        if (id == null || id.isBlank()) {
            author = new Author();
        }
        else {
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

    @GetMapping("/authors/delete")
    public String deleteAuthor(@RequestParam("id") String id, HttpSession session) {
        try {  // удаление автора - проверяем, есть ли книги, если есть - блокируем удаление и выводим ошибку:
            authorService.deleteById(id);
            return "redirect:/authors";
        }
        catch (HasChildEntitiesException e) {
            session.setAttribute("authorIdChildEntitiesError", id);
            session.setAttribute("redirectFromDelete", true);
            return "redirect:/authors";
        }
    }
}
