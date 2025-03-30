package ru.otus.hw.shell;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookCommentMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.models.sql.BookH2;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@SpringBatchTest
class BatchCommandsTest {
    private static final Integer BOOK_COUNT = 3;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EntityManager entityManager;

    private List<BookH2> bookH2List;

    @BeforeEach
    void setUp() {
        bookH2List = entityManager.createQuery(
                "select b from BookH2 b " +
                    "left join fetch b.author " +
                    "left join fetch b.genre", BookH2.class).getResultList();
    }

    @Test
    @DisplayName("Работа джоба миграции")
    void shouldStartMigrationJob() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder().toJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        List<AuthorMongo> authorMongoList = mongoTemplate.findAll(AuthorMongo.class);
        List<GenreMongo> genreMongoList = mongoTemplate.findAll(GenreMongo.class);
        List<BookMongo> bookMongoList = mongoTemplate.findAll(BookMongo.class);
        List<BookCommentMongo> bookCommentMongoList = mongoTemplate.findAll(BookCommentMongo.class);

        assertThat(authorMongoList.size()).isEqualTo(BOOK_COUNT);
        assertThat(genreMongoList.size()).isEqualTo(BOOK_COUNT);
        assertThat(bookMongoList.size()).isEqualTo(BOOK_COUNT);
        assertThat(bookCommentMongoList.size()).isEqualTo(BOOK_COUNT);

        // Проверяем связи для книг
        for (int i = 0; i < BOOK_COUNT; i++) {
            var testedBookMongo = bookMongoList.get(i);
            var testedBookH2 = bookH2List.stream().filter(
                    bookH2 -> bookH2.getTitle().equals(testedBookMongo.getTitle())
            ).findFirst().get();

            assertThat(testedBookMongo.getAuthor().getFullName()).isEqualTo(testedBookH2.getAuthor().getFullName());
            assertThat(testedBookMongo.getGenre().getName()).isEqualTo(testedBookH2.getGenre().getName());
        }
    }
}