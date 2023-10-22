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
public class QuizConfig {

    @Value("${quiz.answers-to-pass}")
    private final Integer answersToPass;
}
