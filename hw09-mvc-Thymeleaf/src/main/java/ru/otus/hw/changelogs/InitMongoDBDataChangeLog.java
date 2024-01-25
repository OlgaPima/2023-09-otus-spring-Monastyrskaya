package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author sKing;
    private Author fKuper;
    private Author lTolstoy;

    private Genre fantastic;
    private Genre adventures;
    private Genre classic;

    private Book book1;
    private Book book3;
    private Book book2;

    @ChangeSet(order = "000", id = "dropDB", author = "olgapima", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "olgapima", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        sKing = repository.save(new Author("1", "С. Кинг", 1947));
        fKuper = repository.save(new Author("2", "Ф. Купер", 1789));
        lTolstoy = repository.save(new Author("3", "Л.Н. Толстой", 1828));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "olgapima", runAlways = true)
    public void initGenres(GenreRepository repository) {
        fantastic = repository.save(new Genre("1", "Фантастика"));
        adventures = repository.save(new Genre("2", "Приключения"));
        classic = repository.save(new Genre("3", "Классика"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "olgapima", runAlways = true)
    public void initBooks(BookRepository repository) {
        book1 = repository.save(new Book("1", "Лангольеры", sKing, fantastic));
        book2 = repository.save(new Book("2", "Зверобой", fKuper, adventures));
        book3 = repository.save(new Book("3", "Война и мир", lTolstoy, classic));
    }

    @ChangeSet(order = "004", id = "initComments", author = "olgapima", runAlways = true)
    public void initComments(BookCommentRepository repository) {
        repository.save(new BookComment("1", "Страшновато как-то", book1));
        repository.save(new BookComment("2", "Книжка супер!", book2));
        repository.save(new BookComment("3", "Школьная литература за 10й класс", book3));
    }
}
