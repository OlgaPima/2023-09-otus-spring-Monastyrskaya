package ru.otus.hw.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class BookComment {
    @Id
    private String id;

    private String commentText;

    @DBRef
    private Book book;

    public BookComment(String commentText, Book book) {
        this.commentText = commentText;
        this.book = book;
    }
}
