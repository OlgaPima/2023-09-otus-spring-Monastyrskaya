package ru.otus.hw.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.models.*;
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

    @PostMapping(value = "/api/v1/genres", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public EntitySaveResult<GenreDto> saveGenre(@Valid @RequestBody GenreDto genreDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new EntitySaveResult<>(bindingResult); // выкидываем ошибки валидации на клиента
        }

        try {
            var savedGenreDto = GenreDto.fromDomainObject(genreService.save(genreDto.toDomainObject()));
            return new EntitySaveResult<>("success", savedGenreDto, null);
        }
        catch (Exception e) {
            return new EntitySaveResult<>("error", null, List.of(new EntitySaveError(null, e.getMessage())));
        }
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public RowDeleteResult deleteGenre(@PathVariable("id") Long id) {
        try {  // удаление жанра - проверяем, есть ли книги, если есть - блокируем удаление и выводим ошибку:
            genreService.deleteById(id);
            return new RowDeleteResult(true);
        }
        catch (HasChildEntitiesException e) {
            return new RowDeleteResult(false);
        }
    }
}
