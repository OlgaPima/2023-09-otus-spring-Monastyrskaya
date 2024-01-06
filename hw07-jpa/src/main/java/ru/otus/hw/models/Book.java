package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // В реализации на ORM и EntityManager загрузка книги без автора и жанра использовалась для обновления и удаления книги.
    // В реализации на JPA книга без автора и жанра уже не нужна, т.к. удаление и обновление выполняются
    // готовым методом из CrudRepository. Соответственно, в данных 2 полях используем EAGER-загрузку, а не LAZY.
    // Одновременно EAGER-загрузка избавляет от необходимости рисовать @NamedEntityGraph,
    // т.к. сразу вытаскивает все связанные сущности одним запросом
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    // А для комментов оставим LAZY
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookComment> comments;

    public Book(Long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
