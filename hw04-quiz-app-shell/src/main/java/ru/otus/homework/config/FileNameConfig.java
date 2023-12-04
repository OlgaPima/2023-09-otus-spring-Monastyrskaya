package ru.otus.homework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties(prefix = "questions")
public class FileNameConfig {
    private HashMap<String, String> fileNameByLocaleTag;

    public FileNameConfig(HashMap<String, String> fileNameByLocaleTag) {
        this.fileNameByLocaleTag = fileNameByLocaleTag;
    }

    public String getFileName(String locale) {
        return fileNameByLocaleTag.get(locale);
    }
}
