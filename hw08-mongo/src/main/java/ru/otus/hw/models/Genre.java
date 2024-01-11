package ru.otus.hw.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Genre {
    @Id
    private String id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
