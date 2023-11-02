package ru.otus.homework.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class QuizConfig {

    private int answersToPass;

    public QuizConfig(@Value("${quiz.answers-to-pass}") int answersToPass) {
        this.answersToPass = answersToPass;
    }
}
