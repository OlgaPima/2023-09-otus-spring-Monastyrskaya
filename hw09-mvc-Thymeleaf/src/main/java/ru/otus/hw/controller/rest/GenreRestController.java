package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.exceptions.HasChildEntitiesException;
import ru.otus.hw.services.GenreService;

@RestController
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreService genreService;

    @DeleteMapping("/genres/delete")
    public boolean deleteGenre(@RequestParam("id") String id) {
        try {  // удаление жанра - проверяем, есть ли книги, если есть - блокируем удаление и выводим ошибку:
            genreService.deleteById(id);
            return true;
        } catch (HasChildEntitiesException e) {
            return false;
        }
    }
}
