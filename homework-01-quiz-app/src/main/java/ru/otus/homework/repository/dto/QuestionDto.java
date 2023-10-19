package ru.otus.homework.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionDto {

    private final int orderNum;
    private final String questionText;
    private final String answerText;
}
