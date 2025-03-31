package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Errors;
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
    @Transactional
    @Secured("ROLE_ADMIN")
    public void deleteById(String id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(id));
        }

        // Удаляем комментарии:
        commentRepository.deleteAllByBookId(id);
        // удаляем саму книгу:
        bookRepository.deleteById(id);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public Book save(Book book) {
        // Проверяем корректность прилетевших с UI родительских объектов - автора и жанра
        // (а то вдруг другой юзер их уже удалил...)
        String authorId = book.getAuthor().getId();
        if (!authorRepository.existsById(authorId)) {
            throw new EntityNotFoundException(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(authorId));
        }

        String genreId = book.getGenre().getId();
        if (!genreRepository.existsById(genreId)) {
            throw new EntityNotFoundException(Errors.GENRE_NOT_FOUND.getMessage().formatted(genreId));
        }

        return bookRepository.save(book);
    }
}
