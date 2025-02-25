package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // В текущей реализации книга без автора и жанра не нужна. Соответственно, в полях author и genre
    // используем EAGER-загрузку, а не LAZY. Одновременно EAGER-загрузка избавляет от необходимости рисовать @NamedEntityGraph,
    // т.к. сразу вытаскивает все связанные сущности одним запросом
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
