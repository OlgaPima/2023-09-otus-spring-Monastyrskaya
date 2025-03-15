package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    private List<Mono<Author>> authors;

    private List<Mono<Genre>> genres;

    private List<Mono<Book>> books;

    @ChangeSet(order = "000", id = "dropDB", author = "olgapima", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "olgapima", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        authors = new ArrayList<>();
        authors.add(repository.save(new Author("1", "С. Кинг", 1947)));
        authors.add(repository.save(new Author("2", "Ф. Купер", 1789)));
        authors.add(repository.save(new Author("3", "Л.Н. Толстой", 1828)));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "olgapima", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genres = new ArrayList<>();
        genres.add(repository.save(new Genre("1", "Фантастика")));
        genres.add(repository.save(new Genre("2", "Приключения")));
        genres.add(repository.save(new Genre("3", "Классика")));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "olgapima", runAlways = true)
    public void initBooks(BookRepository repository) {
        books = new ArrayList<>();
        books.add(repository.save(new Book("1", "Лангольеры", authors.get(0).block(), genres.get(0).block())));
        books.add(repository.save(new Book("2", "Зверобой", authors.get(1).block(), genres.get(1).block())));
        books.add(repository.save(new Book("3", "Война и мир", authors.get(2).block(), genres.get(2).block())));
    }

    @ChangeSet(order = "004", id = "initComments", author = "olgapima", runAlways = true)
    public void initComments(BookCommentRepository repository) {
        repository.save(new BookComment("1", "Страшновато как-то", books.get(0).block())).subscribe();
        repository.save(new BookComment("2", "Книжка супер!", books.get(1).block())).subscribe();
        repository.save(new BookComment("3", "Школьная литература за 10й класс", books.get(2).block())).subscribe();
    }
}
