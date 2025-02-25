package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_comments")
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String commentText;

    @ManyToOne(fetch = FetchType.LAZY) //TODO: логичнее EAGER? какой смысл знать коммент, не зная, к какой он книге?
    @JoinColumn(name = "book_id")
    private Book book;

    public BookComment(String commentText, Book book) {
        this.commentText = commentText;
        this.book = book;
    }
}
