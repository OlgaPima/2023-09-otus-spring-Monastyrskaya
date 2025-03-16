//package ru.otus.hw.controller.rest;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import ru.otus.hw.exceptions.HasChildEntitiesException;
//import ru.otus.hw.services.AuthorService;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthorRestController {
//    private final AuthorService authorService;
//
//    @DeleteMapping("/authors/delete")
//    public boolean deleteAuthor(@RequestParam("id") String id) {
//        try {  // удаление автора - проверяем, есть ли книги, если есть - блокируем удаление и выводим ошибку:
//            authorService.deleteById(id);
//            return true;
//        } catch (HasChildEntitiesException e) {
//            return false;
//        }
//    }
//}
