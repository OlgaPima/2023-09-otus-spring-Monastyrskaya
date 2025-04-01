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
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.mappers.EntityMapper;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.models.sql.GenreH2;
import ru.otus.hw.service.EntityRelationsService;

import java.util.UUID;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class GenreStepConfig {

    private static final int CHUNK_SIZE = 3;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityMapper entityMapper;

    private final EntityRelationsService relationsService;

    private final MongoTemplate mongoTemplate;

    @Bean
    public Step migrateGenreStep(ItemReader<GenreH2> readerGenre, ItemWriter<GenreMongo> writerGenre,
                                   ItemProcessor<GenreH2, GenreMongo> processorGenre) {
        return new StepBuilder("transformGenreStep", jobRepository)
                .<GenreH2, GenreMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerGenre)
                .processor(processorGenre)
                .writer(writerGenre)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Чтение таблицы Genres: ");
                    }

                    @Override
                    public void afterRead(@NonNull GenreH2 genreH2) {
                        log.info("Жанр {} прочитан", genreH2.getName());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Ошибка чтения таблицы Genres: {}" + ex.getMessage());
                    }
                })
                .build();
    }

    @Bean
    public JpaCursorItemReader<GenreH2> readerGenre(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<GenreH2>()
                .name("genreItemReader")
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select g from GenreH2 g")
                .build();
    }

    @Bean
    public ItemProcessor<GenreH2, GenreMongo> processorGenre() {
        return genreH2 -> {
            var genreMongo = entityMapper.toGenreMongo(genreH2);
            genreMongo.setId(UUID.randomUUID().toString());
            relationsService.addGenreIdMapping(genreH2.getId(), genreMongo.getId());
            return genreMongo;
        };
    }

    @Bean
    public MongoItemWriter<GenreMongo> writerGenre() {
        return new MongoItemWriterBuilder<GenreMongo>()
                .template(mongoTemplate)
                .collection("genres")
                .build();
    }
}
