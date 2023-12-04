package ru.otus.homework.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {

    @Test
    @DisplayName("Верно ли проверяется ответ на вопрос")
    public void isAnswerRight() {
        var question = new Question(1, "How many bytes in kilobyte?", "1024");
        assertTrue(question.isAnswerRight("1024"));
    }
}