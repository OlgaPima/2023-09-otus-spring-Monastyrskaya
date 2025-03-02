package ru.otus.hw.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.dto.BookDto;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private String id;

    private String title;

    @DBRef
    private Author author;

    @DBRef
    private Genre genre;

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public BookDto toDtoObject() {
        return new BookDto(id == null || id.isBlank() ? null : id, title, author, genre);
    }
}
