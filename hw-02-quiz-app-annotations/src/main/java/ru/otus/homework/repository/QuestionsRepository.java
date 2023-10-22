package ru.otus.homework.repository;

import ru.otus.homework.repository.dto.QuestionDto;

import java.io.IOException;
import java.util.List;

public interface QuestionsRepository {
    /**
     * Метод чтения данных из файла с вопросами
     * @return
     */
    List<QuestionDto> readAll() throws Exception;
}
