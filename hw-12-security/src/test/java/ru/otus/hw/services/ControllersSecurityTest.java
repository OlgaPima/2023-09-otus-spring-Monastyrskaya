package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.controller.HomeController;
import ru.otus.hw.controller.AuthorController;
import ru.otus.hw.controller.GenreController;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.controller.CommentsController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@DisplayName("Тесты безопасности для контроллеров")
@WebMvcTest(properties = "mongock.enabled=false",
            controllers = {HomeController.class, AuthorController.class, GenreController.class,
                            BookController.class, CommentsController.class})
@Import(SecurityConfiguration.class)
public class ControllersSecurityTest {

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

    private final User testUser = new User(null, "user", "testPass", List.of("ADMIN"));

    @BeforeEach
    public void mock() {
        when(bookService.findById("1")).thenReturn(Optional.of(
                new Book("1", "Лангольеры",
                        new Author("1", "С. Кинг", 1947),
                        new Genre("1", "Фантастика"))
                )
        );
    }

    @Test
    @DisplayName("Страница логина доступна для всех")
    public void shouldAccessToLoginPageForAll() throws Exception {
        MockHttpServletRequestBuilder request = createRequest(HttpMethod.GET, "/login", null);
        mvc.perform(request)
                .andExpectAll(status().isOk());
        mvc.perform(request.with(user(testUser.getUsername())
                                .authorities(new SimpleGrantedAuthority("ROLE_" + testUser.getRoles().get(0)))
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("Разрешаем доступ авторизованным пользователям")
    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("getEndpointsList")
    public void shouldAccessWithTestUser(HttpMethod httpMethod, String urlTemplate, Map<String, String> queryParams,
                                         String redirectUrl) throws Exception {
        MockHttpServletRequestBuilder request = createRequest(httpMethod, urlTemplate, queryParams);
        var authority = new SimpleGrantedAuthority("ROLE_" + testUser.getRoles().get(0));

        if (redirectUrl == null || redirectUrl.isBlank()) {
            mvc.perform(request.with(user(testUser.getUsername()).authorities(authority)))
                    .andExpect(status().isOk());
        } else {
            mvc.perform(request.with(user(testUser.getUsername()).authorities(authority)))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl(redirectUrl));
        }
    }

    @DisplayName("Запрещаем доступ неавторизованным пользователям")
    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("getEndpointsList")
    public void shouldNotAccessWithoutUser(HttpMethod httpMethod, String url,
                                           Map<String, String> queryParams) throws Exception {
        MockHttpServletRequestBuilder request = createRequest(httpMethod, url, queryParams);
        mvc.perform(request)
                .andExpectAll(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @MethodSource("getEndpointsList")
    public static Stream<Arguments> getEndpointsList() {
        return  Stream.of(
                Arguments.of(HttpMethod.GET, "/authors", null, null),
                Arguments.of(HttpMethod.GET, "/authors/edit", null, null),
                Arguments.of(HttpMethod.POST, "/authors/edit", null, null),
                Arguments.of(HttpMethod.POST, "/authors/delete", Map.of("id", "1"), "/authors"),
                Arguments.of(HttpMethod.GET, "/genres", null, null),
                Arguments.of(HttpMethod.GET, "/genres/edit", null, null),
                Arguments.of(HttpMethod.POST, "/genres/edit", null, null),
                Arguments.of(HttpMethod.POST, "/genres/delete", Map.of("id", "1"), "/genres"),

                Arguments.of(HttpMethod.GET, "/", null, null),
                Arguments.of(HttpMethod.GET, "/books", null, null),
                Arguments.of(HttpMethod.GET, "/books/edit", null, null),
                Arguments.of(HttpMethod.POST, "/books/edit", null, null),
                Arguments.of(HttpMethod.POST, "/books/delete", Map.of("id", "1"), "/books"),

                Arguments.of(HttpMethod.GET, "/books/comments", Map.of("bookId", "1"), null),
                Arguments.of(HttpMethod.POST, "/books/comments", Map.of("bookId", "1"), null),
                Arguments.of(HttpMethod.POST, "/books/comments/edit",
                        Map.of("bookId", "1", "commentId", "1"), "/books/comments?bookId=1"),
                Arguments.of(HttpMethod.POST, "/books/comments/delete",
                        Map.of("commentId", "1", "bookId", "1"), "/books/comments?bookId=1")
        );
    }

    private static MockHttpServletRequestBuilder createRequest(HttpMethod httpMethod, String urlTemplate,
                                                               Map<String, String> queryParams) {
        MockHttpServletRequestBuilder request;

        if (HttpMethod.GET.equals(httpMethod)) {
            request = get(urlTemplate);
        } else if (HttpMethod.POST.equals(httpMethod)) {
            request = post(urlTemplate);
        } else if (HttpMethod.DELETE.equals(httpMethod)) {
            request = delete(urlTemplate);
        } else {
            return fail("Неподдерживаемый метод!");
        }

        if (queryParams != null) {
            MultiValueMap<String, String> mvmQueryParams = new LinkedMultiValueMap<>();
            queryParams.forEach(mvmQueryParams::add);
            request.queryParams(mvmQueryParams);
        }
        return request;
    }
}
