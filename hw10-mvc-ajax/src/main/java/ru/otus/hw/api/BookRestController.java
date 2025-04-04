package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.EntitySaveError;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.RowDeleteResultDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> booksList() {
        return bookService.findAll().stream()
                .map(BookDto::fromDomainObject)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v1/books/{id}")
    public Book getBookById(@PathVariable(required = false) Long id) {
        return bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage()));
    }

    @PostMapping(value = "/api/v1/books", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public EntitySaveResult<BookDto> saveBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем ошибки валидации на клиента
        }

        var savedBookDto = bookService.save(bookDto);
        return new EntitySaveResult<>(SaveResults.SUCCESS.getName(), savedBookDto, null);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public RowDeleteResultDto deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id); // может вылететь EntityNotFoundException
        return new RowDeleteResultDto(true);
    }

    @ExceptionHandler({Exception.class, EntityNotFoundException.class})
    public EntitySaveResult<BookDto> cannotSaveOrDeleteBook(Exception ex) {
        return new EntitySaveResult<>(SaveResults.ERROR.getName(), null,
                List.of(new EntitySaveError(null, ex.getMessage())));
    }
}
