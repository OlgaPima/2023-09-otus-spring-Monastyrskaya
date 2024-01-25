package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private String id;

    @NotBlank(message = "Поле обязательно для заполнения")
    @Size(min = 2, max = 100, message = "Длина значения должна быть от 2 до 100 символов")
    private String fullName;

    @Min(value = 1000, message = "Минимальное значение = 1000")
    @Max(value = 2015, message = "Максимальное значение = 2015")
    private Integer birthYear;

    public Author toDomainObject() {
        // при создании записи из браузера прилетает пустая строка в виде айдишника - меняем ее на Null для корректного сохранения в БД
        return new Author(id.isBlank() ? null : id, fullName, birthYear);
    }
}
