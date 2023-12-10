package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        String sql = """
                select b.id, b.title, b.author_id, a.full_name as author_name,
                   b.genre_id, g.name as genre_name
                from books as b
                   inner join authors as a on b.author_id = a.id
                   inner join genres as g on b.genre_id = g.id
                where b.id = :bookId""";

        Map<String, Object> params = Collections.singletonMap("bookId", id);
        try {
            // здесь .ofNullable, а не .of, только чтобы идея не подсвечивала, что может быть null.
            // Так-то, если прилетит пустой датасет, то свалимся в эксепшн маппера, т.е. .ofNullable тут по сути не нужен
            return Optional.ofNullable(jdbcOperations.queryForObject(sql, params, new BookRowMapper()));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = """
                select b.id, b.title, b.author_id, a.full_name as author_name,
                   b.genre_id, g.name as genre_name
                from books as b
                   inner join authors as a on b.author_id = a.id
                   inner join genres as g on b.genre_id = g.id""";
        return jdbcOperations.query(sql, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var params = new MapSqlParameterSource(Map.of("id", id));
        int affectedRows = jdbcOperations.update("delete from books where id = :id", params);

        // Выбрасываем EntityNotFoundException, если не найдено такой записи в БД:
        if (affectedRows == 0) {
            throw new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(id));
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into books(title, author_id, genre_id) values(:title, :author_id, :genre_id)",
                createMapParametersFromBook(book, false), keyHolder, new String[]{"id"});
        book.setId(keyHolder.getKeyAs(Long.class));

        return book;
    }

    private Book update(Book book) {
        int affectedRows = jdbcOperations.update(
                "update books set title=:title, author_id=:author_id, genre_id=:genre_id " +
                "where id=:id",
                createMapParametersFromBook(book, true));

        // Выбрасываем EntityNotFoundException, если не обновлено ни одной записи в БД:
        if (affectedRows == 0) {
            throw new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(book.getId()));
        }
        return book;
    }

    private MapSqlParameterSource createMapParametersFromBook(Book book, boolean includeId) {
        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        if (includeId) {
            params.addValue("id", book.getId());
        }
        return params;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");

            var author = new Author(rs.getInt("author_id"), rs.getString("author_name"));
            var genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));

            return new Book(id, title, author, genre);
        }
    }
}
