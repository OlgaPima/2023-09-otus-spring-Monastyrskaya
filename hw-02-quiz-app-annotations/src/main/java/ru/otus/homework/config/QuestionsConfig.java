package ru.otus.homework.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class QuestionsConfig {
    @Value("${questions.file-path}")
    private final String filePath;

    @Value("${questions.file-charset}")
    private final String fileCharset;

    @Value("${questions.fields-delimiter}")
    private final String fieldsDelimiter;
}
