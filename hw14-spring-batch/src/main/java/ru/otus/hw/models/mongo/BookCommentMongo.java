package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bookComments")
public class BookCommentMongo {
    @Id
    private String id;

    @Field(name = "comment")
    private String comment;

    @DBRef
    private BookMongo book;

    public BookCommentMongo(String comment, BookMongo book) {
        this.comment = comment;
        this.book = book;
    }
}
