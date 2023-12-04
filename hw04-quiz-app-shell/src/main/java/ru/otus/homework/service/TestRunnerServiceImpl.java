package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Student;
import ru.otus.homework.service.abstractions.QuizService;
import ru.otus.homework.service.abstractions.ResultService;
import ru.otus.homework.service.abstractions.TestRunnerService;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final QuizService quizService;
    private final ResultService resultService;

    @Override
    public void run(Student student) {
        var testResult = quizService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}