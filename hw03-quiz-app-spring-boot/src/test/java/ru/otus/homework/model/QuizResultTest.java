package ru.otus.homework.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.homework.config.AppConfig;
import ru.otus.homework.service.abstractions.QuizService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class QuizResultTest {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private QuizService quizService;

    private List<Question> questions;

    @BeforeEach
    public void setUp() {
        questions = quizService.getAllQuestions();
    }

    @Test
    @DisplayName("Проверка подсчета правильных ответов")
    public void getRightAnswersCountTest() {
        assertEquals(3, createQuizResult().getRightAnswersCount());
    }

    @Test
    @DisplayName("Проверка признака прохождения теста")
    public void checkTestPassed() {
        assertTrue(createQuizResult().isTestPassed());
    }

    private QuizResult createQuizResult() {
        var quizResult = new QuizResult(new Student("Ivan", "Petrov"), appConfig);
        boolean isEnglish = Objects.equals(appConfig.getLocale(), "en-US");

        var q1 = getQuestionByNum(1);
        quizResult.addAnswer(q1, isEnglish ? "Barcelona?..." : "Барселона, кажецца...");

        var q2 = getQuestionByNum(2);
        quizResult.addAnswer(q2, isEnglish ? "Everest" : "Эверест");

        var q3 = getQuestionByNum(3);
        quizResult.addAnswer(q3, isEnglish ? "I don't know..." : "Хз...");

        var q4 = getQuestionByNum(4);
        quizResult.addAnswer(q4, isEnglish ? "Gagarin" : "Гагарин");

        var q5 = getQuestionByNum(5);
        quizResult.addAnswer(q5, "1024");

        return quizResult;
    }

    private Question getQuestionByNum(int num) {
        return questions.stream().filter(q -> q.getOrderNum() == num).findFirst().get();
    }
}
