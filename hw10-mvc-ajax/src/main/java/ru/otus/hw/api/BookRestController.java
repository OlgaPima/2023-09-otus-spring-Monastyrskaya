package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.*;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<Book> booksList() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public Book getBookById(@PathVariable(required = false) Long id) {
        return bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage()));
    }

    @PostMapping(value = "/api/v1/books", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public EntitySaveResult<BookDto> saveBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем ошибки валидации на клиента
        }

        try {
            var savedBookDto = BookDto.fromDomainObject(bookService.save(bookDto.toDomainObject()));
            return new EntitySaveResult<>("success", savedBookDto, null);
        }
        catch (Exception e) {
            return new EntitySaveResult<>("error", null, List.of(new EntitySaveError(null, e.getMessage())));
        }
    }

    @DeleteMapping("/api/v1/books/{id}")
    public RowDeleteResult deleteBook(@PathVariable("id") Long id) {
        try {
            bookService.deleteById(id);
            return new RowDeleteResult(true);
        }
        catch (HasChildEntitiesException e) {
            return new RowDeleteResult(false);
        }
    }
}
