package ru.otus.hw.services;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@DataMongoTest
//@WebMvcTest(AuthorController.class)
// - приложение на MongoDB, и эти 2 аннотации несовместимы друг с другом. Поэтому для тестирования эндпоинтов на встроенной монге используем 3 аннотации ниже:
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@DisplayName("BookController:")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("отображение списка всех книг")
    public void shouldReturnCorrectBooksList() throws Exception {
        List<Book> books = List.of(
                new Book("1", "Book1", new Author("1", "Author1", 1967), new Genre("1", "genre1")),
                new Book("2", "Book2", new Author("2", "Author2", 1994), new Genre("2", "genre2"))
        );
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("booksList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books))
                .andExpect(content().string(containsString("Book1")))
                .andExpect(content().string(containsString("Book2")));
    }

    @Test
    @DisplayName("удаление книги и редирект на список книг")
    public void shouldDeleteBookAndRedirectToBooksList() throws Exception {
        mvc.perform(get("/books/delete?id=1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).deleteById(eq("1"));
    }

    @Test
    @DisplayName("создание новой книги и редирект на список книг")
    public void shouldCreateBookAndRedirectToBooksList() throws Exception {
        var bookDto = new BookDto("1", "Book1",
                new Author("1", "AuthorName1", 1876),
                new Genre("1", "Genre1")
        );
        mvc.perform(post("/books/edit")
                        .flashAttr("book", bookDto))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).save(bookDto.toDomainObject());
    }

    @Test
    @DisplayName("редактирование книги и редирект на список книг")
    public void shouldEditBookAndRedirectToBooksList() throws Exception {
        var newBook = new BookDto("1", "Book1",
                new Author("1", "AuthorName1", 1876),
                new Genre("1", "Genre1")
        );

        this.mvc.perform(post("/books/edit?id=1")
                        .flashAttr("book", newBook))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).save(newBook.toDomainObject());
    }
}