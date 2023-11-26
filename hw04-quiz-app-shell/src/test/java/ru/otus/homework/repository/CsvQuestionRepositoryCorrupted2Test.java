package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.homework.exception.CsvParsingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties =
        {"questions.file-name-by-locale-tag.ru-RU = CorruptedFile2.csv",
        "questions.file-name-by-locale-tag.en-US = CorruptedFile2.csv"}
)
public class CsvQuestionRepositoryCorrupted2Test {

    @Autowired
    private CsvQuestionsRepository csvQuestionsRepository;

    @Test
    @DisplayName("Чтение испорченного csv-файла (некорректный номер вопроса)")
    public void checkReadingCorruptedCsvFile2() {
        Exception e = assertThrows(CsvParsingException.class, () -> csvQuestionsRepository.findAll());
        assertNotNull(e);
        assertThat(e.getMessage()).startsWith("Недопустимый порядковый номер вопроса");
    }

}
