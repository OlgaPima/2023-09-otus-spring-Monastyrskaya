package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCommentDto {
    private String id;

    @NotBlank(message = "Поле обязательно для заполнения")
    private String commentText;

    private Book book;
}
