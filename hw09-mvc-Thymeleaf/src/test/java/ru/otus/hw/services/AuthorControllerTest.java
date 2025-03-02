package ru.otus.hw.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("AuthorController: ")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorServiceImpl authorService;

    private final List<Author> authors = List.of(
            new Author("1", "С. Кинг", 1947),
            new Author("2", "Ф. Купер", 1789),
            new Author("3", "Л.Н. Толстой", 1828)
    );

    @Test
    @DisplayName("отображение списка всех авторов")
    public void shouldReturnCorrectAuthorsList() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        this.mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorsList"))
                .andExpect(model().attribute("authors", authors));
    }

    @Test
    @DisplayName("отображение страницы редактирования автора")
    public void shouldFillAuthorEditPage() throws Exception {
        var testAuthor = authors.get(0);
        when(authorService.findById("1")).thenReturn(testAuthor);
        mvc.perform(get("/authors/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorEdit"))
                .andExpect(model().attribute("author", testAuthor))
                .andExpect(content().string(containsString(testAuthor.getFullName())));
    }

    @Test
    @DisplayName("сохранение нового автора и переадресация на список всех авторов")
    public void shouldCreateAuthorAndRedirectToAuthorsList() throws Exception {
        var testAuthor = authors.get(0);
        this.mvc.perform(post("/authors/edit")
                        .flashAttr("author", testAuthor.toDtoObject()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService, times(1)).save(eq(testAuthor));
    }

    @Test
    @DisplayName("нельзя сохранять автора с ошибками")
    public void shouldNotCreateAuthorWithError() throws Exception {
        var authorDto = new AuthorDto("1", "", 1993);
        this.mvc.perform(post("/authors/edit")
                        .flashAttr("author", authorDto))
                .andExpect(status().isOk())
                .andExpect(view().name("authorEdit"));

        verify(authorService, times(0)).save(eq(authorDto.toDomainObject()));
    }

    @Test
    @DisplayName("удаление автора и переадресация на список всех авторов")
    public void shouldDeleteAuthorAndRedirectToAuthorsList() throws Exception {
        String authorId = "1";
        mvc.perform(get("/authors/delete?id=" + authorId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService, times(1)).deleteById(eq(authorId));
    }
}
