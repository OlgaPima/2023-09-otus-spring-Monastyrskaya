package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.common.Errors;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookCommentRepository commentRepository;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insert(String title, String authorId, String genreId) {
        return save(title, authorId, genreId);
    }

    @Override
    public Book update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(id));
        }
        // Удаляем комментарии:
        commentRepository.deleteAllByBookId(id);
        // удаляем саму книгу:
        bookRepository.deleteById(id);
    }

    private Book save(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    private Book save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId).orElseThrow(
                () -> new EntityNotFoundException(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(authorId)));
        var genre = genreRepository.findById(genreId).orElseThrow(
                () -> new EntityNotFoundException(Errors.GENRE_NOT_FOUND.getMessage().formatted(genreId)));

        Book book;
        if (id == null || id.isBlank()) {
            book = new Book(title, author, genre);
        } else {
            book = bookRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(id)));
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
        }

        return bookRepository.save(book);
    }
}
