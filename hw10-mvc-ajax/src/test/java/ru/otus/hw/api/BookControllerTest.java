package ru.otus.hw.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@DisplayName("BookRestController tests: ")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Author author1 = new Author(1L, "Author1", null);

    private final Genre genre1 = new Genre(1L, "Genre1");

    @Test
    @DisplayName("отображение списка всех книг")
    public void shouldReturnCorrectBooksList() throws Exception {
        List<Book> booksList = List.of(new Book(1L, "Book1", author1, genre1),
                    new Book(2L, "Book2", author1, genre1));

        when(bookService.findAll()).thenReturn(booksList);
        List<BookDto> booksDto = booksList.stream().map(BookDto::fromDomainObject).toList();

        this.mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(booksDto)));
    }

    @Test
    @DisplayName("удаление книги")
    public void shouldDeleteBook() throws Exception {
        Long bookId = 1L;
        mvc.perform(delete("/api/v1/books/{id}", bookId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("создание новой книги")
    public void shouldCreateBook() throws Exception {
        var createdBookDto = new BookDto(null, "Book1", author1, genre1);
        var expectedBookDto = new BookDto("1", "Book1", author1, genre1);

        when(bookService.save(any())).thenReturn(expectedBookDto);
        var saveResult = new EntitySaveResult<>(SaveResults.SUCCESS.getName(), expectedBookDto, null);
        String expectedApiResult = objectMapper.writeValueAsString(saveResult);

        mvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdBookDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedApiResult));

        verify(bookService, times(1)).save(any());
    }

    @Test
    @DisplayName("редактирование существующей книги")
    public void shouldEditBook() throws Exception {
        String id = "1";
        var existingBook = new BookDto(id, "Book1", author1, genre1);
        when(bookService.findById(Long.valueOf(id))).thenReturn(Optional.of(existingBook.toDomainObject()));

        this.mvc.perform(get("/api/v1/books/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Book1")))
                .andExpect(content().string(containsString(author1.getFullName())))
                .andExpect(content().string(containsString(genre1.getName())));
    }
}
