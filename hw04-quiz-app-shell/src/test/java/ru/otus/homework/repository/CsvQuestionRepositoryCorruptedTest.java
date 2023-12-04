package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.homework.exception.CsvParsingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties =
        {"questions.file-name-by-locale-tag.ru-RU = CorruptedFile.csv",
        "questions.file-name-by-locale-tag.en-US = CorruptedFile.csv"}
)
public class CsvQuestionRepositoryCorruptedTest {

    @Autowired
    private CsvQuestionsRepository csvQuestionsRepository;

    @Test
    @DisplayName("Чтение испорченного csv-файла (2 колонки вместо 3)")
    public void checkReadingCorruptedCsvFile() {
        // Чтобы следующая строка корректно отработала, нам необходимы файлы локализации в тестовом контексте.
        // Но в них достаточно иметь только 1 сообщение - то, с которым падает репозиторий в данном тесте
        Exception e = assertThrows(CsvParsingException.class, () -> csvQuestionsRepository.findAll());
        assertNotNull(e);
        assertThat(e.getMessage()).startsWith("Не указаны все необходимые атрибуты вопроса");
    }
}
