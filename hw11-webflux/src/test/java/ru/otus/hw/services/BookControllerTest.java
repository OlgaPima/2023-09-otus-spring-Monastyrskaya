package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@DisplayName("BookController:")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    private final List<Book> books = List.of(
            new Book("1", "Book1", new Author("1", "Author1", 1967), new Genre("1", "genre1")),
            new Book("2", "Book2", new Author("2", "Author2", 1994), new Genre("2", "genre2"))
    );

    // TODO: переделать
    /*@Test
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
    }*/

    @Test
    @DisplayName("удаление книги и редирект на список книг")
    public void shouldDeleteBookAndRedirectToBooksList() throws Exception {
        mvc.perform(get("/books/delete?id=1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
        // TODO: разобраться
//        verify(bookService, times(1)).deleteById(eq("1"));
    }

    // TODO: разобраться
//    @Test
//    @DisplayName("создание новой книги и редирект на список книг")
//    public void shouldCreateBookAndRedirectToBooksList() throws Exception {
//        mvc.perform(post("/books/edit")
//                        .flashAttr("book", books.get(0).toDtoObject()))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/books"));
//
//        verify(bookService, times(1)).save(books.get(0));
//    }
//
//    @Test
//    @DisplayName("редактирование книги и редирект на список книг")
//    public void shouldEditBookAndRedirectToBooksList() throws Exception {
//        mvc.perform(post("/books/edit?id=1")
//                        .flashAttr("book", books.get(0).toDtoObject()))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/books"));
//
//        verify(bookService, times(1)).save(books.get(0));
//    }
}