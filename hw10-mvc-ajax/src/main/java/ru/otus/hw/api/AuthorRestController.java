package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.*;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping(value = "/api/v1/authors", produces = "application/json;charset=UTF-8")
    public List<AuthorDto> authorsList() {
        return authorService.findAll().stream()
                .map(AuthorDto::fromDomainObject)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/api/v1/authors/{id}")
    public AuthorDto getAuthorById(@PathVariable(required = false) Long id) {
        return AuthorDto.fromDomainObject(authorService.findById(id));
    }

    @PostMapping(value = "/api/v1/authors", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public EntitySaveResult<AuthorDto> saveAuthor(@Valid @RequestBody AuthorDto authorDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем ошибки валидации на клиента
        }

        try {
            var savedAuthorDto = authorService.save(authorDto);
            return new EntitySaveResult<>("success", savedAuthorDto, null);
        }
        catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
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
