package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.RowDeleteResult;
import ru.otus.hw.models.EntitySaveError;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<Author> authorsList() {
        return authorService.findAll();
    }

    @GetMapping("/api/v1/authors/{id}")
    public AuthorDto getAuthorById(@PathVariable(required = false) Long id) {
        return AuthorDto.fromDomainObject(authorService.findById(id));
    }

    @PostMapping("/api/v1/authors")
    public EntitySaveResult<AuthorDto> saveAuthor(@Valid AuthorDto authorDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем ошибки валидации на клиента
        }

        try {
            var savedAuthorDto = AuthorDto.fromDomainObject(authorService.save(authorDto.toDomainObject()));
            return new EntitySaveResult<>("success", savedAuthorDto, null);
        }
        catch (Exception e) {
            return new EntitySaveResult<>("error", null, List.of(new EntitySaveError(null, e.getMessage())));
        }
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public RowDeleteResult deleteAuthor(@PathVariable("id") Long id) {
        try {  // удаление автора - проверяем, есть ли книги, если есть - блокируем удаление и выводим ошибку:
            authorService.deleteById(id);
            return new RowDeleteResult(true);
        }
        catch (HasChildEntitiesException e) {
            return new RowDeleteResult(false);
        }
    }
}
