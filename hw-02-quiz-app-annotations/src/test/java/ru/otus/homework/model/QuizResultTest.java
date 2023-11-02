package ru.otus.homework.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.config.QuizConfig;

import static org.junit.jupiter.api.Assertions.*;

class QuizResultTest {

    @Test
    @DisplayName("Проверка подсчета правильных ответов")
    public void getRightAnswersCountTest() {
        assertEquals(3, getQuizResultWith3RightAnswers().getRightAnswersCount());
    }

    @Test
    @DisplayName("Проверка признака прохождения теста")
    public void checkTestPassed() {
        assertTrue(getQuizResultWith3RightAnswers().isTestPassed());
    }

    private QuizResult getQuizResultWith3RightAnswers() {
        var quizResult = new QuizResult(new Student("Ivan", "Petrov"), new QuizConfig(3));

        var q1 = new Question(1, "What is the capital of Australia?", "Sidney");
        quizResult.addAnswer(q1, "Barselona?...");
        var q2 = new Question(2, "What is the highest mountain in the world?", "Everest");
        quizResult.addAnswer(q2, "Everest");
        var q3 = new Question(3, "What is the deepest point of the world ocean?", "Mariana Trench");
        quizResult.addAnswer(q3, "I don't know...");
        var q4 = new Question(4, "What is the surname of the first cosmonaut in the world?", "Gagarin");
        quizResult.addAnswer(q4, "Gagarin");
        var q5 = new Question(5, "How many byte in kilobyte?", "1024");
        quizResult.addAnswer(q5, "1024");

        return quizResult;
    }
}