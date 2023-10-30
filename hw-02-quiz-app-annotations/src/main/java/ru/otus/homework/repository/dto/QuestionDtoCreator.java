package ru.otus.homework.repository.dto;

import org.springframework.stereotype.Component;
import ru.otus.homework.exception.CsvParsingException;

import java.util.Arrays;
import java.util.List;

@Component
public class QuestionDtoCreator {

    public QuestionDto parseFromCsv(String csvString, String delimiter) throws CsvParsingException {
        List<String> questionAttributes = Arrays.asList(csvString.split(delimiter));
        if (questionAttributes.size() < 3) {
            throw new CsvParsingException("Не указаны все необходимые атрибуты вопроса: порядковый номер, текст вопроса, текст ответа");
        }

        int orderNum;
        try {
            orderNum = Integer.parseInt(questionAttributes.get(0));
        }
        catch (NumberFormatException e) {
            throw new CsvParsingException("Недопустимый порядковый номер вопроса (должен быть целым числом)");
        }

        String questionText = questionAttributes.get(1);
        if (questionText == null || questionText.isBlank()) {
            throw new CsvParsingException("Не указан текст вопроса");
        }

        String answerText = questionAttributes.get(2);
        if (answerText == null || answerText.isBlank()) {
            throw new CsvParsingException("Не указан текст ответа");
        }

        return new QuestionDto(orderNum, questionText, answerText);
    }
}
