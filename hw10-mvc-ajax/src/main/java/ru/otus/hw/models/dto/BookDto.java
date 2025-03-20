package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String id;

    @NotBlank(message = "Поле обязательно для заполнения")
    @Size(min = 2, max = 100, message = "Длина значения должна быть от 2 до 100 символов")
    private String title;

    @NotNull(message = "Поле обязательно для заполнения")
    private Author author;

    @NotNull(message = "Поле обязательно для заполнения")
    private Genre genre;

    public Book toDomainObject() {
        return new Book(id == null || id.isBlank() ? null : Long.parseLong(id), title, author, genre);
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId() == null ? null : book.getId().toString(),
                book.getTitle(), book.getAuthor(), book.getGenre());
    }
}
