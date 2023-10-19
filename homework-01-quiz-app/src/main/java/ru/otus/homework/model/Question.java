package ru.otus.homework.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Question {

    private final int orderNum;
    private final String questionText;
    private final String answerText;


//    @Override
//    public String toString() {
//        return "Question{" +
//                "questionText='" + questionText + '\'' +
//                ", answerText=" + answerText +
//                '}';
//    }
}
