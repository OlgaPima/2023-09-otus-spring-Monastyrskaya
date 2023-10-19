package ru.otus.homework.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.homework.config.QuizConfig;
import ru.otus.homework.repository.dto.QuestionDto;
import ru.otus.homework.repository.dto.QuestionDtoCreator;
import ru.otus.homework.exception.CsvParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionsRepository implements QuestionsRepository {

    private final QuizConfig quizConfig;

    @Getter
    private List<QuestionDto> questionsList;

    @Override
    public List<QuestionDto> readAll() throws Exception {
        if (questionsList == null) {
            questionsList = new ArrayList<>();
        }
        else {
            questionsList.clear();
        }

        int rowNumber = 0;
        try (InputStream csvStream = getClass().getClassLoader().getResourceAsStream(quizConfig.getQuestionsFilePath());
             InputStreamReader csvStreamReader = new InputStreamReader(csvStream, quizConfig.questionsFileCharset);
             BufferedReader reader = new BufferedReader(csvStreamReader);
        )
        {
            String row;
            while ((row = reader.readLine()) != null) {
                rowNumber++;
                var questionDto = QuestionDtoCreator.parseFromCsv(row, quizConfig.getQuestionFieldsDelimiter());
                questionsList.add(questionDto);
            }
            return questionsList;
        }
        catch (CsvParsingException e) {
            System.out.println(String.format("Csv file parsing error in line %s: %s", rowNumber, e.getMessage()));
            e.printStackTrace();
            throw e;
        }
        catch (IOException e) {
            System.out.println("Error reading the question file: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
