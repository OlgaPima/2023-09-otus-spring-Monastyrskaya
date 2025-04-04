package ru.otus.hw.models.mongo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
public class AuthorMongo {
    @Id
    private String id;

    @Field(name = "full_name")
    private String fullName;

    public AuthorMongo(String fullName) {
        this.fullName = fullName;
    }
}
