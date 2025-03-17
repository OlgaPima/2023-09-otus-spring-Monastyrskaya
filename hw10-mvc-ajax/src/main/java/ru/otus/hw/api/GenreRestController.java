package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.EntitySaveError;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<Genre> genresList() {
        return genreService.findAll();
    }

    @GetMapping("/api/v1/genres/{id}")
    public GenreDto getGenreById(@PathVariable(required = false) Long id) {
        return GenreDto.fromDomainObject(genreService.findById(id));
    }

    @PostMapping(value = "/api/v1/genres", consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public EntitySaveResult<GenreDto> saveGenre(@Valid @RequestBody GenreDto genreDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем ошибки валидации на клиента
        }

        var savedGenreDto = genreService.save(genreDto);
        return new EntitySaveResult<>(SaveResults.SUCCESS.getName(), savedGenreDto, null);
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public boolean deleteGenre(@PathVariable("id") Long id) {
        try {  // удаление жанра - проверяем, есть ли книги, если есть - блокируем удаление и выводим ошибку:
            genreService.deleteById(id);
            return true;
        } catch (HasChildEntitiesException e) {
            return false;
        }
    }

    @ExceptionHandler(Exception.class)
    public EntitySaveResult<GenreDto> cannotSaveGenre(Exception ex) {
        return new EntitySaveResult<>(SaveResults.ERROR.getName(), null,
                List.of(new EntitySaveError(null, ex.getMessage())));
    }
}
