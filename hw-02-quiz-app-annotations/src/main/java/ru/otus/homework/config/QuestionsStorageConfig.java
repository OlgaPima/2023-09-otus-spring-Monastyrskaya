package ru.otus.homework.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class QuestionsStorageConfig {
    private String filePath;
    private String fieldsDelimiter;
    private String fileCharset;

    public QuestionsStorageConfig(@Value("${questions-storage.file-path}") String filePath,
                                  @Value("${questions-storage.fields-delimiter}") String fieldsDelimiter,
                                  @Value("${questions-storage.file-charset}") String fileCharset) {
        this.filePath = filePath;
        this.fieldsDelimiter = fieldsDelimiter;
        this.fileCharset = fileCharset;
    }
}
