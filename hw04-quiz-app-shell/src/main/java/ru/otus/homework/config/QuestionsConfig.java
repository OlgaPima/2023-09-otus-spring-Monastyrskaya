package ru.otus.homework.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class QuestionsConfig {

    private final String fieldsDelimiter;
    private final String fileCharset;
    private final String questionsFileName;
    private final String locale;

    public QuestionsConfig(@Value("${questions.fields-delimiter}") String fieldsDelimiter,
                           @Value("${questions.file-charset}") String fileCharset,
                           FileNameConfig fileNameConfig,
                           @Value("${application.locale}") String locale) {
        this.fieldsDelimiter = fieldsDelimiter;
        this.fileCharset = fileCharset;
        this.questionsFileName = fileNameConfig.getFileName(locale);
        this.locale = locale;
    }
}
