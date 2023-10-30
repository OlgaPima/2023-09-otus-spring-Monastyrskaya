package ru.otus.homework.repository;

import org.junit.jupiter.api.*;
import ru.otus.homework.config.QuestionsStorageConfig;
import ru.otus.homework.exception.CsvParsingException;
import ru.otus.homework.repository.CsvQuestionsRepository;
import ru.otus.homework.repository.dto.QuestionDto;
import ru.otus.homework.repository.dto.QuestionDtoCreator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj .core.api.Assertions.assertThat;

public class CsvQuestionRepositoryTest {

    private static final String CSV_DELIMITER = ";";
    private static final String CSV_CHARSET = "UTF-8";

    @Test
    @DisplayName("Чтение корректного csv-файла с 2 вопросами")
    public void checkReadingGoodCsvFile() throws Exception {
        var csvQuestionsRepository = createTestRepository("NormalFile.csv");
        List<QuestionDto> questions = csvQuestionsRepository.findAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Чтение пустого csv-файла")
    public void checkReadingEmptyCsvFile() throws Exception {
        var csvQuestionsRepository = createTestRepository("EmptyFile.csv");
        List<QuestionDto> questions = csvQuestionsRepository.findAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Чтение испорченного csv-файла (2 колонки вместо 3)")
    public void checkReadingCorruptedCsvFile() {
        var csvQuestionsRepository = createTestRepository("CorruptedFile.csv");
        Exception e = assertThrows(CsvParsingException.class, () -> csvQuestionsRepository.findAll());
        assertThat(e.getMessage()).startsWith("Не указаны все необходимые атрибуты вопроса");
    }

    @Test
    @DisplayName("Чтение испорченного csv-файла (некорректный номер вопроса)")
    public void checkReadingCorroptedCsvFile2() {
        var csvQuestionsRepository = createTestRepository("CorruptedFile2.csv");
        Exception e = assertThrows(CsvParsingException.class, () -> csvQuestionsRepository.findAll());
        assertThat(e.getMessage()).startsWith("Недопустимый порядковый номер вопроса");
    }

    private CsvQuestionsRepository createTestRepository(String fileName) {
        var quizConfig = new QuestionsStorageConfig(fileName, CSV_DELIMITER, CSV_CHARSET);
        return new CsvQuestionsRepository(quizConfig, new QuestionDtoCreator());
    }
}