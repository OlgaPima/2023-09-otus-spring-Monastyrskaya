package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.homework.exception.CsvParsingException;
import ru.otus.homework.repository.dto.QuestionDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {"questions.file-name-by-locale-tag.ru-RU = NormalFile.csv",
        "questions.file-name-by-locale-tag.en-US = NormalFile.csv"})
public class CsvQuestionRepositoryNormalTest {

    @Autowired
    private CsvQuestionsRepository csvQuestionsRepository;

    @Test
    @DisplayName("Чтение корректного csv-файла с 2 вопросами")
    public void checkReadingGoodCsvFile() {
        List<QuestionDto> questions = csvQuestionsRepository.findAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(2);
    }

    private CsvQuestionsRepository createTestRepository(String fileName) {
//        var ioService = Mockito.mock(LocalizedIOService.class);

//        var fileNameConfig = Mockito.mock(FileNameConfig.class);
//        Mockito.when(fileNameConfig.getFileName("ru-RU")).thenReturn(fileName);
//        Mockito.when(fileNameConfig.getFileName("en-US")).thenReturn(fileName);
//        var questionsConfig = new QuestionsConfig(";", "UTF-8", fileNameConfig, "ru-RU");

//        return new CsvQuestionsRepository(questionsConfig, new QuestionDtoCreator(), ioService);
        return null;
    }
}