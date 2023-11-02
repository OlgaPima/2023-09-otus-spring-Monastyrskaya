package ru.otus.homework.service;

import ru.otus.homework.model.Question;
import ru.otus.homework.model.QuizResult;
import ru.otus.homework.model.Student;

public interface QuizService {

    void askQuestions();
    QuizResult executeTestFor(Student student);
}
