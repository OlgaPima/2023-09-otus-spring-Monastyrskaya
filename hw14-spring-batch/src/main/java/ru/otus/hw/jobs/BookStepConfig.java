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
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.sql.BookH2;
import ru.otus.hw.service.EntityRelationsService;

import java.util.UUID;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class BookStepConfig {

    private static final int CHUNK_SIZE = 3;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityMapper entityMapper;

    private final EntityRelationsService relationsService;

    private final MongoTemplate mongoTemplate;

    @Bean
    public Step migrateBookStep(ItemReader<BookH2> readerBook,
                                  ItemWriter<BookMongo> writerBook,
                                  ItemProcessor<BookH2, BookMongo> processorBook) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<BookH2, BookMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerBook)
                .processor(processorBook)
                .writer(writerBook)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Чтение таблицы Books: ");
                    }

                    @Override
                    public void afterRead(@NonNull BookH2 bookH2) {
                        log.info("Книга {} прочитана", bookH2.getTitle());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Ошибка чтения таблицы Books: {}", ex.getMessage());
                    }
                }).build();
    }

    @Bean
    public ItemReader<BookH2> readerBook(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<BookH2>()
                .name("bookItemReader")
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select b from BookH2 b")
                .build();
    }

    @Bean
    public ItemProcessor<BookH2, BookMongo> processorBook() {
        return bookH2 -> {
            var bookMongo = entityMapper.toBookMongo(bookH2);
            bookMongo.setId(UUID.randomUUID().toString());
            relationsService.addBookIdMapping(bookH2.getId(), bookMongo.getId());

            bookMongo.setAuthor(relationsService.createAuthorMongo(bookH2.getAuthor()));
            bookMongo.setGenre(relationsService.createGenreMongo(bookH2.getGenre()));
            return bookMongo;
        };
    }

    @Bean
    public ItemWriter<BookMongo> writerBook() {
        return new MongoItemWriterBuilder<BookMongo>()
                .template(mongoTemplate)
                .collection("books")
                .build();
    }
}
