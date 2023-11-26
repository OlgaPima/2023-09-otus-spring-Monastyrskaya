package ru.otus.homework.service.abstractions;

import ru.otus.homework.model.Question;
import ru.otus.homework.model.QuizResult;
import ru.otus.homework.model.Student;

import java.util.List;

public interface QuizService {

    List<Question> getAllQuestions();
    QuizResult executeTestFor(Student student);
}
