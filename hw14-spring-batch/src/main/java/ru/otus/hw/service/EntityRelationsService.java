package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.models.sql.AuthorH2;
import ru.otus.hw.models.sql.BookH2;
import ru.otus.hw.models.sql.GenreH2;

import java.util.HashMap;
import java.util.Map;

@Service
public class EntityRelationsService {
    private final Map<Long, String> authorIdMapping = new HashMap<>();

    private final Map<Long, String> genreIdMapping = new HashMap<>();

    private final Map<Long, String> bookIdMapping = new HashMap<>();

    public void addAuthorIdMapping(Long h2Id, String mongoId) {
        authorIdMapping.put(h2Id, mongoId);
    }

    public void addGenreIdMapping(Long h2Id, String mongoId) {
        genreIdMapping.put(h2Id, mongoId);
    }

    public void addBookIdMapping(Long h2Id, String mongoId) {
        bookIdMapping.put(h2Id, mongoId);
    }

    public AuthorMongo createAuthorMongo(AuthorH2 authorH2) {
         return new AuthorMongo(authorIdMapping.get(authorH2.getId()), authorH2.getFullName());
    }

    public GenreMongo createGenreMongo(GenreH2 genreH2) {
        return new GenreMongo(genreIdMapping.get(genreH2.getId()), genreH2.getName());
    }

    public BookMongo createBookMongo(BookH2 bookH2) {
        return new BookMongo(bookIdMapping.get(bookH2.getId()), bookH2.getTitle(),
                createAuthorMongo(bookH2.getAuthor()), createGenreMongo(bookH2.getGenre()));
    }

}
