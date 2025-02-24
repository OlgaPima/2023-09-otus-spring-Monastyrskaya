package ru.otus.hw.services;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


//@DataMongoTest
//@WebMvcTest(AuthorController.class)
// - приложение на MongoDB, и эти 2 аннотации несовместимы друг с другом. Поэтому для тестирования эндпоинтов на встроенной монге используем 3 аннотации ниже:
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@DisplayName("AuthorController: ")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @DisplayName("отображение списка всех авторов")
    @Test
    public void shouldReturnCorrectAuthorsList() throws Exception {
        List<Author> authors = List.of(new Author("1", "С. Кинг", 1947),
                new Author("2", "Ф. Купер", 1789),
                new Author("3", "Л.Н. Толстой", 1828));

        when(authorService.findAll()).thenReturn(authors);

        this.mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("authorsList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("authors"))
                .andExpect(content().string(containsString("С. Кинг")))
                .andExpect(content().string(containsString("Ф. Купер")))
                .andExpect(content().string(containsString("Л.Н. Толстой")));
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

    @Test
    @DisplayName("создание автора и переадресация на список всех авторов")
    public void shouldCreateAuthorAndRedirectToAuthorsList() throws Exception {
        var authorDto = new AuthorDto("1", "Author1", 1993);

        this.mvc.perform(post("/authors/edit")
                        .flashAttr("author", authorDto))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService, times(1)).save(eq(authorDto.toDomainObject()));
    }

    @DisplayName("отображение автора для редактирования")
    @Test
    public void shouldFillAuthorEditPage() throws Exception {
        String id = "1";
        var author = new AuthorDto(id, "Author1", 1993);
        when(authorService.findById(id)).thenReturn(author.toDomainObject());

        this.mvc.perform(get("/authors/edit?id=" + id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("authorEdit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("author"))
                .andExpect(content().string(containsString("Author1")));
    }
}
