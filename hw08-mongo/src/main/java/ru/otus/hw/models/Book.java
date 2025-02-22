package ru.otus.hw.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
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
        this(null, title, author, genre);
    }

    public Book(String id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
