package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Errors;
import ru.otus.hw.models.dto.BookDto;
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
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Errors.BOOK_NOT_FOUND.getMessage().formatted(id)));
        // Удаляем комментарии:
        commentRepository.deleteAllByBookId(id);
        // удаляем саму книгу:
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto save(BookDto bookDto) {
        // Проверяем корректность прилетевших с UI родительских объектов - автора и жанра
        // (а то вдруг другой юзер их уже удалил...)
        Long authorId = bookDto.getAuthor().getId();
        authorRepository.findById(authorId).orElseThrow(() ->
                new EntityNotFoundException(Errors.AUTHOR_NOT_FOUND.getMessage().formatted(authorId)));
        Long genreId = bookDto.getGenre().getId();
        genreRepository.findById(genreId).orElseThrow(() ->
                new EntityNotFoundException(Errors.GENRE_NOT_FOUND.getMessage().formatted(genreId)));

        var book = bookDto.toDomainObject();
        return BookDto.fromDomainObject(bookRepository.save(book));
    }
}
