package ru.otus.hw.jobs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookCommentMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class MigrationJobConfig {

    public static final String JOB_NAME = "migrationJob";

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job migrationJob(Step startUpStep, Step migrateAuthorStep, Step migrateGenreStep, Step migrateBookStep,
                            Step migrateCommentStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(startUpStep).on("FAILED").end()
                .next(migrateAuthorStep)
                .next(migrateGenreStep)
                .next(migrateBookStep)
                .next(migrateCommentStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        log.info("{} - перенос данных библиотеки с H2 на MongoDB", JOB_NAME);
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        log.info("{} - библиотека успешно перенесена на MongoDB", JOB_NAME);
                    }
                })
                .build();
    }

    @Bean
    public Step startUpStep(MongoTemplate mongoTemplate) {
        return new StepBuilder("startUpStep", jobRepository)
                .tasklet(startUpTasklet(mongoTemplate), transactionManager)
                .build();
    }

    @Bean
    public Tasklet startUpTasklet(MongoTemplate mongoTemplate) {
        return (contribution, chunkContext) -> {
            log.info("Очистка MongoDB перед началом миграции...");

            // Очищаем целевую БД (монгу) перед миграцией
            mongoTemplate.remove(new Query(), AuthorMongo.class, "authors");
            mongoTemplate.remove(new Query(), GenreMongo.class, "genres");
            mongoTemplate.remove(new Query(), BookMongo.class, "books");
            mongoTemplate.remove(new Query(), BookCommentMongo.class, "bookComments");

            log.info("Очистка MongoDB выполнена успешно");
            return RepeatStatus.FINISHED;
        };
    }
}
