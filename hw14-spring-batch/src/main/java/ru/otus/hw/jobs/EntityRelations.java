package ru.otus.hw.jobs;

import java.util.HashMap;
import java.util.Map;

/**
 * Синглтон для сопоставления айдишников сущностей между H2 и MongoDB
 */
public class EntityRelations {
    private static EntityRelations instance;

    private final Map<Long, String> authorIdMapping = new HashMap<>();

    private final Map<Long, String> genreIdMapping = new HashMap<>();

    private final Map<Long, String> bookIdMapping = new HashMap<>();


    private EntityRelations() {
    }

    public static EntityRelations getInstance() {
        if (instance == null) {
            instance = new EntityRelations();
        }
        return instance;
    }

    public void addAuthorIdMapping(Long h2Id, String mongoId) {
        authorIdMapping.put(h2Id, mongoId);
    }

    public void addGenreIdMapping(Long h2Id, String mongoId) {
        genreIdMapping.put(h2Id, mongoId);
    }

    public void addBookIdMapping(Long h2Id, String mongoId) {
        bookIdMapping.put(h2Id, mongoId);
    }

    public String getAuthorMongoId(Long authorH2Id) {
        return authorIdMapping.get(authorH2Id);
    }

    public String getGenreMongoId(Long genreH2Id) {
        return genreIdMapping.get(genreH2Id);
    }

    public String getBookMongoId(Long bookH2Id) {
        return bookIdMapping.get(bookH2Id);
    }
}
