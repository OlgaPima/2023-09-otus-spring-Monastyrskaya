package ru.otus.hw.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Author {
    @Id
    private String id;

    private String fullName;

    private List<Book> books;

    public Author(String fullName) {
        this.fullName = fullName;
    }
}
