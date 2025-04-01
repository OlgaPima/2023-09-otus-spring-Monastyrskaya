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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.mappers.EntityMapper;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.sql.AuthorH2;
import ru.otus.hw.service.EntityRelationsService;

import java.util.UUID;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AuthorStepConfig {

    private static final int CHUNK_SIZE = 3;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityMapper entityMapper;

    private final EntityRelationsService relationsService;

    private final MongoTemplate mongoTemplate;

    @Bean
    public Step migrateAuthorStep(ItemReader<AuthorH2> readerAuthor, ItemWriter<AuthorMongo> writerAuthor,
                                  ItemProcessor<AuthorH2, AuthorMongo> processorAuthor) {
        return new StepBuilder("migrateAuthorStep", jobRepository)
                .<AuthorH2, AuthorMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerAuthor)
                .processor(processorAuthor)
                .writer(writerAuthor)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Nтение таблицы Authors: ");
                    }

                    @Override
                    public void afterRead(@NonNull AuthorH2 authorH2) {
                        log.info("Автор {} прочитан", authorH2.getFullName());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Ошибка чтения таблицы Authors: {}", ex.getMessage());
                    }
                })
                .build();
    }

    @Bean
    public ItemReader<AuthorH2> readerAuthor(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<AuthorH2>()
                .name("authorItemReader")
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select a from AuthorH2 a")
                .build();
    }

    @Bean
    public ItemProcessor<AuthorH2, AuthorMongo> processorAuthor() {
        return authorH2 -> {
            var authorMongo = entityMapper.toAuthorMongo(authorH2);
            authorMongo.setId(UUID.randomUUID().toString());
            relationsService.addAuthorIdMapping(authorH2.getId(), authorMongo.getId());
            return authorMongo;
        };
    }

    @Bean
    public ItemWriter<AuthorMongo> writerAuthor() {
        return new MongoItemWriterBuilder<AuthorMongo>()
                .template(mongoTemplate)
                .collection("authors")
                .build();
    }
}
