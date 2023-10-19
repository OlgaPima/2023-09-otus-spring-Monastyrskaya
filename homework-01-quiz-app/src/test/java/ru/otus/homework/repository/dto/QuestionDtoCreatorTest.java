package ru.otus.homework.repository.dto;

import org.junit.jupiter.api.*;
import ru.otus.homework.config.QuizConfig;
import ru.otus.homework.exception.CsvParsingException;
import ru.otus.homework.repository.CsvQuestionsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj .core.api.Assertions.assertThat;

public class QuestionDtoCreatorTest {

    private static final String CSV_DELIMITER = ";";
    private static final String CSV_CHARSET = "UTF-8";

    @Test
    @DisplayName("Чтение корректного csv-файла с 2 вопросами")
    public void checkReadingGoodCsvFile() throws Exception {
        var quizConfig = new QuizConfig("NormalFile.csv", CSV_DELIMITER, CSV_CHARSET);
        var csvQuestionsRepository = new CsvQuestionsRepository(quizConfig);
        List<QuestionDto> questions = csvQuestionsRepository.readAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Чтение пустого csv-файла")
    public void checkReadingEmptyCsvFile() throws Exception {
        var quizConfig = new QuizConfig("EmptyFile.csv", CSV_DELIMITER, CSV_CHARSET);
        var csvQuestionsRepository = new CsvQuestionsRepository(quizConfig);
        List<QuestionDto> questions = csvQuestionsRepository.readAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Чтение испорченного csv-файла (2 колонки вместо 3)")
    public void checkReadingCorruptedCsvFile() {
        var quizConfig = new QuizConfig("CorruptedFile.csv", CSV_DELIMITER, CSV_CHARSET);
        var csvQuestionsRepository = new CsvQuestionsRepository(quizConfig);
        Exception e = assertThrows(CsvParsingException.class, () -> csvQuestionsRepository.readAll());
        assertThat(e.getMessage()).startsWith("Не указаны все необходимые атрибуты вопроса");
    }

    @Test
    @DisplayName("Чтение испорченного csv-файла (некорректный номер вопроса)")
    public void checkReadingCorroptedCsvFile2() {
        var quizConfig = new QuizConfig("CorruptedFile2.csv", CSV_DELIMITER, CSV_CHARSET);
        var csvQuestionsRepository = new CsvQuestionsRepository(quizConfig);
        Exception e = assertThrows(CsvParsingException.class, () -> csvQuestionsRepository.readAll());
        assertThat(e.getMessage()).startsWith("Недопустимый порядковый номер вопроса");
    }
}