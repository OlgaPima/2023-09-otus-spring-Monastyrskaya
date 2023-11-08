package ru.otus.homework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Component
@ConfigurationProperties(prefix = "questions")
public class QuestionsConfig {
    @Getter
    private String fieldsDelimiter;
    @Getter
    private String fileCharset;

    private HashMap<String, String> fileNameByLocaleTag;

    @Autowired
    private AppConfig appConfig; //через поле - нехорошо, но иначе придется либо объединять AppConfig и QuestionsConfig,
    // либо выставлять наружу сеттер для данного поля, что тоже нехорошо

    public String getQuestionsFileName() {
        return fileNameByLocaleTag.get(appConfig.getLocale());
    }

}
