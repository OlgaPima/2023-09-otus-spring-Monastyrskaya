package ru.otus.hw.api;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.EntitySaveResult;
import ru.otus.hw.models.SaveResults;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorServiceImpl;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AuthorRestController.class)
@DisplayName("AuthorRestController tests: ")
public class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("отображение списка всех авторов")
    public void shouldReturnCorrectAuthorsList() throws Exception {
        List<Author> authors = List.of(new Author(1L, "С. Кинг", 1947),
                new Author(2L, "Ф. Купер", 1789),
                new Author(3L, "Л.Н. Толстой", 1828));

        when(authorService.findAll()).thenReturn(authors);
        List<AuthorDto> authorsDto = authors.stream().map(AuthorDto::fromDomainObject).toList();

        this.mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorsDto)));
    }

    @Test
    @DisplayName("удаление автора")
    public void shouldDeleteAuthor() throws Exception {
        Long authorId = 1L;
        mvc.perform(delete("/api/v1/authors/{id}", authorId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authorService, times(1)).deleteById(authorId);
    }

    @Test
    @DisplayName("создание нового автора")
    public void shouldCreateAuthor() throws Exception {
        var createdAuthorDto = new AuthorDto(null, "Author1", 1993);
        var expectedAuthorDto = new AuthorDto("1", "Author1", 1993);

        when(authorService.save(createdAuthorDto)).thenReturn(expectedAuthorDto);
        var saveResult = new EntitySaveResult<>("success", expectedAuthorDto, null);
        String expectedApiResult = objectMapper.writeValueAsString(saveResult);

        mvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdAuthorDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedApiResult));

        verify(authorService, times(1)).save(createdAuthorDto); //save(createdAuthor);
    }

    @Test
    @DisplayName("редактирование существующего автора")
    public void shouldEditAuthor() throws Exception {
//        String id = "1";
//        var author = new AuthorDto(id, "Author1", 1993);
//        when(authorService.findById(Long.valueOf(id))).thenReturn(author.toDomainObject());
//
//        this.mvc.perform(get("/authors/edit?id=" + id))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("authorEdit"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("author"))
//                .andExpect(content().string(containsString("Author1")));
    }
}
