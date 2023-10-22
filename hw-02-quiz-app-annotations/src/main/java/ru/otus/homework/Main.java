package ru.otus.homework;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.service.QuizService;

@ComponentScan
public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Main.class);
        var quizService = context.getBean(QuizService.class);
        quizService.askQuestions();
    }
}
