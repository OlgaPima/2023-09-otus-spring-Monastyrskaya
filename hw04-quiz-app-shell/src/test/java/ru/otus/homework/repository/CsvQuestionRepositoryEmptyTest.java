package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.homework.repository.dto.QuestionDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {"questions.file-name-by-locale-tag.ru-RU = EmptyFile.csv",
        "questions.file-name-by-locale-tag.en-US = EmptyFile.csv"})
public class CsvQuestionRepositoryEmptyTest {

    @Autowired
    private CsvQuestionsRepository csvQuestionsRepository;

    @Test
    @DisplayName("Чтение пустого csv-файла")
    public void checkReadingEmptyCsvFile() {
        List<QuestionDto> questions = csvQuestionsRepository.findAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(0);
    }

}
