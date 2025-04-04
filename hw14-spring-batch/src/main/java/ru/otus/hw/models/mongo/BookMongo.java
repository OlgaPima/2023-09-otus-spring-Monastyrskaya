package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class BookMongo {
    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "author_id")
    private AuthorMongo author;

    @Field(name = "genre_id")
    private GenreMongo genre;

    public BookMongo(String title, AuthorMongo author, GenreMongo genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
