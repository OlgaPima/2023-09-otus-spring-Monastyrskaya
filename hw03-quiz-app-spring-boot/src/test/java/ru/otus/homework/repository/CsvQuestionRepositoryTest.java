package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.homework.config.FileNameConfig;
import ru.otus.homework.config.QuestionsConfig;
import ru.otus.homework.exception.CsvParsingException;
import ru.otus.homework.repository.dto.QuestionDto;
import ru.otus.homework.repository.dto.QuestionDtoCreator;
import ru.otus.homework.service.abstractions.LocalizedIOService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvQuestionRepositoryTest {

    @Test
    @DisplayName("Чтение корректного csv-файла с 2 вопросами")
    public void checkReadingGoodCsvFile() {
        var csvQuestionsRepository = createTestRepository("NormalFile.csv");
        List<QuestionDto> questions = csvQuestionsRepository.findAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Чтение пустого csv-файла")
    public void checkReadingEmptyCsvFile() {
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
    public void checkReadingCorruptedCsvFile2() {
        var csvQuestionsRepository = createTestRepository("CorruptedFile2.csv");
        Exception e = assertThrows(CsvParsingException.class, () -> csvQuestionsRepository.findAll());
        assertThat(e.getMessage()).startsWith("Недопустимый порядковый номер вопроса");
    }

    private CsvQuestionsRepository createTestRepository(String fileName) {
        var ioService = Mockito.mock(LocalizedIOService.class);

        var fileNameConfig = Mockito.mock(FileNameConfig.class);
        Mockito.when(fileNameConfig.getFileName("ru-RU")).thenReturn(fileName);
        Mockito.when(fileNameConfig.getFileName("en-US")).thenReturn(fileName);
        var questionsConfig = new QuestionsConfig(";", "UTF-8", fileNameConfig, "ru-RU");

        return new CsvQuestionsRepository(questionsConfig, new QuestionDtoCreator(), ioService);
    }
}