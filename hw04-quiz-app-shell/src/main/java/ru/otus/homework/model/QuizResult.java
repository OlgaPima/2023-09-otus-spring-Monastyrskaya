package ru.otus.homework.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class QuizResult {
    @Getter
    private final Student student;
    /**
     * Количество правильных ответов для прохождения теста
     */
    private final int answersCntToPassTest;

    private Map<Question, String> answers = new HashMap<>();

    public void addAnswer(Question question, String answer) {
        answers.put(question, answer);
    }

    public boolean isTestPassed() {
        return getRightAnswersCount() >= answersCntToPassTest;
    }

    public int getRightAnswersCount() {
        return Math.toIntExact(answers.entrySet().stream().filter(entry -> entry.getKey().isAnswerRight(entry.getValue())).count());
    }
}
