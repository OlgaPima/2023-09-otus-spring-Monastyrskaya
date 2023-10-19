package ru.otus.homework.config;

import lombok.Getter;

import java.nio.charset.Charset;

@Getter
public class QuizConfig {
    public final String questionsFilePath;
    public final String questionFieldsDelimiter;
    public final Charset questionsFileCharset;

    //TODO (ревьюерам): в этом классе могут быть еще какие-то настройки опроса, не относящиеся именно к файлу.
    // Поэтому 3 свойства выше обозваны с префиксом "questions". В перспективе их можно выделить в отдельный класс QuestionsFileConfig,
    // а данный класс расширить другими настройками (задел на будущее)

    public QuizConfig(String questionsFilePath, String questionFieldsDelimiter, String charset) {
        this.questionsFilePath = questionsFilePath;
        this.questionFieldsDelimiter = questionFieldsDelimiter;
        this.questionsFileCharset = Charset.forName(charset);
    }
}
