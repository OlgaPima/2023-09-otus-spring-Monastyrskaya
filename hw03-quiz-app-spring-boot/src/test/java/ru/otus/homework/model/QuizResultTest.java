package ru.otus.homework.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.homework.config.AppConfig;
import ru.otus.homework.config.FileNameConfig;
import ru.otus.homework.config.QuestionsConfig;
import ru.otus.homework.repository.CsvQuestionsRepository;
import ru.otus.homework.repository.dto.QuestionDtoCreator;
import ru.otus.homework.service.QuizServiceImpl;
import ru.otus.homework.service.abstractions.LocalizedIOService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuizResultTest {

    private AppConfig appConfig;
    private List<Question> questions = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        appConfig = new AppConfig(2, "ru-RU");

        var fileNameConfig = Mockito.mock(FileNameConfig.class);
        Mockito.when(fileNameConfig.getFileName("ru-RU")).thenReturn("localization/questions_RU.csv");
        Mockito.when(fileNameConfig.getFileName("en-US")).thenReturn("localization/questions_EN.csv");

        var questionsConfig = new QuestionsConfig(";", "UTF-8", fileNameConfig, "ru-RU");
        var ioService = Mockito.mock(LocalizedIOService.class);
        var questionsRepository = new CsvQuestionsRepository(questionsConfig, new QuestionDtoCreator(), ioService);
        var quizService = new QuizServiceImpl(questionsRepository, appConfig, ioService);
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
        var quizResult = new QuizResult(new Student("Ivan", "Petrov"), appConfig.getAnswersToPass());
        boolean isEnglish = Objects.equals(appConfig.getLocale(), "en-US");

        var q1 = getQuestionByNum(1);
        quizResult.addAnswer(q1, isEnglish ? "Barcelona?..." : "Барселона, кажецца...");

        var q2 = getQuestionByNum(2);
        quizResult.addAnswer(q2, isEnglish ? "Everest" : "Эверест");

        var q3 = getQuestionByNum(3);
        quizResult.addAnswer(q3, "1945");

        var q4 = getQuestionByNum(4);
        quizResult.addAnswer(q4, isEnglish ? "I don't know..." : "Хз...");

        var q5 = getQuestionByNum(5);
        quizResult.addAnswer(q5, isEnglish ? "Lenin" : "Ленин");

        return quizResult;
    }

    private Question getQuestionByNum(int num) {
        return questions.stream().filter(q -> q.getOrderNum() == num).findFirst().get();
    }


}
