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
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/books")
    public String booksList(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "booksList";
    }

    @GetMapping("/books/edit")
    public String editGenre(@RequestParam(required = false) String id, Model model) {
        Book book;
        if (id == null || id.isBlank()) {
            book = new Book();
        }
        else {
            book = bookService.findById(id).orElseThrow(
                    () -> new EntityNotFoundException(Errors.GENRE_NOT_FOUND.getMessage()));
        }
        model.addAttribute("book", book);
        model.addAttribute("authorsList", authorService.findAll());
        model.addAttribute("genresList", genreService.findAll());

        return "bookEdit";
    }

    @PostMapping("/books/edit")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authorsList", authorService.findAll());
            model.addAttribute("genresList", genreService.findAll());
            return "bookEdit";
        }
        bookService.save(bookDto.toDomainObject());
        return "redirect:/books";
    }

    @GetMapping("/books/delete")
    public String deleteBook(@RequestParam("id") String id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}