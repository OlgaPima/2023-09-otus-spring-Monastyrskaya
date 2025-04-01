package ru.otus.hw.jobs;

import jakarta.persistence.EntityManagerFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.mappers.EntityMapper;
import ru.otus.hw.models.mongo.BookCommentMongo;
import ru.otus.hw.models.sql.BookCommentH2;
import ru.otus.hw.service.EntityRelationsService;

import java.util.UUID;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class CommentsStepConfig {

    private static final int CHUNK_SIZE = 3;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityMapper entityMapper;

    private final EntityRelationsService relationsService;

    private final MongoTemplate mongoTemplate;

    @Bean
    public Step migrateCommentStep(JpaCursorItemReader<BookCommentH2> readerComment,
                                     MongoItemWriter<BookCommentMongo> writerComment,
                                     ItemProcessor<BookCommentH2, BookCommentMongo> processorComment) {
        return new StepBuilder("transformCommentStep", jobRepository)
                .<BookCommentH2, BookCommentMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerComment)
                .processor(processorComment)
                .writer(writerComment)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Чтение таблицы Comments:");
                    }

                    @Override
                    public void afterRead(@NonNull BookCommentH2 bookCommentH2) {
                        log.info("Комментарий с id={} прочитан", bookCommentH2.getId());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Ошибка чтения таблицы Comments: " + ex.getMessage());
                    }
                }).build();
    }

    @Bean
    public JpaCursorItemReader<BookCommentH2> readerComment(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<BookCommentH2>()
                .name("commentItemReader")
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select c from BookCommentH2 c")
                .build();
    }

    @Bean
    public ItemProcessor<BookCommentH2, BookCommentMongo> processorComment() {
        return commentH2 -> {
            var commentMongo = entityMapper.toCommentMongo(commentH2);
            commentMongo.setId(UUID.randomUUID().toString());
            commentMongo.setBook(relationsService.createBookMongo(commentH2.getBook()));
            return commentMongo;
        };
    }

    @Bean
    public MongoItemWriter<BookCommentMongo> writerComment() {
        return new MongoItemWriterBuilder<BookCommentMongo>()
                .template(mongoTemplate)
                .collection("bookComments")
                .build();
    }
}

