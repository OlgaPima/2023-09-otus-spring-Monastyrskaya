package ru.otus.homework.repository.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.exception.CsvParsingException;
import ru.otus.homework.service.abstractions.LocalizedMessagesService;

import java.util.Arrays;
import java.util.List;

@Component
public class QuestionDtoCreator {

    @Autowired // чтобы в тестах не заморачиваться созданием LocalizedMessagesService, если прокидывать его через конструктор
    private LocalizedMessagesService localizedMessagesService;

    public QuestionDto parseFromCsv(String csvString, String delimiter) throws CsvParsingException {
        List<String> questionAttributes = Arrays.asList(csvString.split(delimiter));
        if (questionAttributes.size() < 3) {
            throw new CsvParsingException(localizedMessagesService.getMessage("questions.noAttributeException"));
        }

        int orderNum;
        try {
            orderNum = Integer.parseInt(questionAttributes.get(0));
        }
        catch (NumberFormatException e) {
            throw new CsvParsingException(localizedMessagesService.getMessage("questions.invalidQuestionNo"));
        }

        String questionText = questionAttributes.get(1);
        if (questionText == null || questionText.isBlank()) {
            throw new CsvParsingException(localizedMessagesService.getMessage("questions.noQuestionText"));
        }

        String answerText = questionAttributes.get(2);
        if (answerText == null || answerText.isBlank()) {
            throw new CsvParsingException(localizedMessagesService.getMessage("questions.noAnswerText"));
        }

        return new QuestionDto(orderNum, questionText, answerText);
    }
}
