package ru.otus.homework.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Question {

    private final int orderNum;
    private final String questionText;
    private final String rightAnswerText;

    public boolean isAnswerRight(String answerText) {
        return answerText.trim().equalsIgnoreCase(rightAnswerText.trim());
    }
}
