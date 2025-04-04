package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.BookCommentMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.models.sql.AuthorH2;
import ru.otus.hw.models.sql.BookH2;
import ru.otus.hw.models.sql.BookCommentH2;
import ru.otus.hw.models.sql.GenreH2;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    AuthorMongo toAuthorMongo(AuthorH2 authorH2);

    GenreMongo toGenreMongo(GenreH2 genreH2);

    BookMongo toBookMongo(BookH2 bookH2);

    BookCommentMongo toCommentMongo(BookCommentH2 bookCommentH2);
}
