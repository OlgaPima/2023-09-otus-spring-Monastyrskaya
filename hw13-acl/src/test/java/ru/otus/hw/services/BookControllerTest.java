package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;

import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("BookController:")
@WebMvcTest(properties = "mongock.enabled=false", controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false) // (отключаем авторизацию)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    private final List<Book> books = List.of(
            new Book("1", "Book1", new Author("1", "Author1", 1967), new Genre("1", "genre1")),
            new Book("2", "Book2", new Author("2", "Author2", 1994), new Genre("2", "genre2"))
    );

    @Test
    @DisplayName("отображение списка всех книг")
    public void shouldReturnCorrectBooksList() throws Exception {
        given(bookService.findAll()).willReturn(books);
        mvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("booksList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books))
                .andExpect(content().string(containsString("Book1")))
                .andExpect(content().string(containsString("Book2")));
    }

/*    @Test
    @DisplayName("удаление книги и редирект на список книг")
    public void shouldDeleteBookAndRedirectToBooksList() throws Exception {
        mvc.perform(get("/books/delete?id=1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).deleteById(eq("1"));
    }*/

    @Test
    @DisplayName("создание новой книги и редирект на список книг")
    public void shouldCreateBookAndRedirectToBooksList() throws Exception {
        mvc.perform(post("/books/edit")
                        .flashAttr("book", books.get(0).toDtoObject()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).save(books.get(0));
    }

    @Test
    @DisplayName("редактирование книги и редирект на список книг")
    public void shouldEditBookAndRedirectToBooksList() throws Exception {
        mvc.perform(post("/books/edit?id=1")
                        .flashAttr("book", books.get(0).toDtoObject()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).save(books.get(0));
    }

    @Test
    @DisplayName("удаление книги с комментариями и редирект на список книг")
    void shouldDeleteBook() throws Exception {
        String bookId = "1";
        var book = new Book(bookId, new Author("1", "Author1", 1986), new Genre("1", "Genre1"));
        when(bookService.findById(bookId)).thenReturn(Optional.of(book));
        when(commentService.findCommentsByBookId(bookId))
                .thenReturn(List.of(new BookComment("1", "Где вы это откопали и как это развидеть?", book)));

        mvc.perform(post("/books/delete")
                        .param("id", bookId))
                .andExpect(view().name("redirect:/books"));

        verify(bookService, times(1)).deleteById(bookId);
    }
}