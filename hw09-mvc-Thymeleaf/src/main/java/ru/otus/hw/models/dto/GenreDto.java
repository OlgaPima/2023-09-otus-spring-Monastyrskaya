package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private String id;

    @NotBlank(message = "Поле обязательно для заполнения")
    @Size(min = 2, max = 100, message = "Длина значения должна быть от 2 до 100 символов")
    private String name;

    public Genre toDomainObject() {
        // при создании записи из браузера прилетает пустая строка в виде айдишника - меняем ее на Null для корректного сохранения в БД
        return new Genre(id.isBlank() ? null : id, name);
    }
}
