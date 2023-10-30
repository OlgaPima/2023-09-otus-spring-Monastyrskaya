package ru.otus.homework.repository;

import ru.otus.homework.repository.dto.QuestionDto;

import java.util.List;

public interface QuestionsRepository {
    /**
     * Метод чтения данных из файла с вопросами
     * @return
     */
    List<QuestionDto> findAll() throws Exception;
}
