package ru.otus.homework;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.service.QuizService;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        QuizService quizService = context.getBean(QuizService.class);
        quizService.askQuestions();

        // Данная операция в принципе не нужна. Мы не работаем здесь с БД, а Spring Boot сделает закрытие за нас
        context.close();
    }
}
