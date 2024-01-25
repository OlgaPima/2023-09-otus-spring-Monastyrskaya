package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.AuthorController;
import ru.otus.hw.exceptions.HasChildEntitiesException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan({"ru.otus.hw.services"})
//@DataMongoTest
@WebMvcTest(AuthorController.class)
//@DisplayName("AuthorController: ")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @Test
//    @DisplayName("должен блокировать удаление автора, если у него есть книги")
    public void shouldNotDeleteAuthorHavingBooks() throws Exception {
//        String authorId = authorService.findAll().stream().findFirst().get().getId();
//        assertThrows(HasChildEntitiesException.class, () -> authorService.deleteById(authorId));
//        given(authorService.deleteById("1")).willThrow(HasChildEntitiesException.class);

        mvc.perform(get("/authors/delete?id=1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/authors"));

//        verify(authorService, times(1))
//                .deleteById(eq(1L));
    }
}
