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
        sKing = repository.save(new Author("С. Кинг"));
        fKuper = repository.save(new Author("Ф. Купер"));
        lTolstoy = repository.save(new Author("Л.Н. Толстой"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "olgapima", runAlways = true)
    public void initGenres(GenreRepository repository) {
        fantastic = repository.save(new Genre("Фантастика"));
        adventures = repository.save(new Genre("Приключения"));
        classic = repository.save(new Genre("Классика"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "olgapima", runAlways = true)
    public void initBooks(BookRepository repository) {
        book1 = repository.save(new Book("Лангольеры", sKing, fantastic));
        book2 = repository.save(new Book("Зверобой", fKuper, adventures));
        book3 = repository.save(new Book("Война и мир", lTolstoy, classic));
    }

    @ChangeSet(order = "004", id = "initComments", author = "olgapima", runAlways = true)
    public void initComments(BookCommentRepository repository) {
        repository.save(new BookComment("Страшновато как-то", book1));
        repository.save(new BookComment("Книжка супер!", book2));
        repository.save(new BookComment("Школьная литература за 10й класс", book3));
    }
}
