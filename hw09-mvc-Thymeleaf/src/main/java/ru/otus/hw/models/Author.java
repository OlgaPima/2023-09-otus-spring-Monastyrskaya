package ru.otus.hw.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    private String id;

    private String fullName;

    private Integer birthYear;

//    private List<Book> books; // TODO: зачем это было?

    public Author(String fullName, Integer birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
    }
}
