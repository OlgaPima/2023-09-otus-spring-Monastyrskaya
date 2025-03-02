package ru.otus.hw.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.dto.AuthorDto;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    private String id;

    private String fullName;

    private Integer birthYear;

    public Author(String fullName, Integer birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    public AuthorDto toDtoObject() {
        return new AuthorDto(id == null || id.isBlank() ? null : id, fullName, birthYear);
    }
}
