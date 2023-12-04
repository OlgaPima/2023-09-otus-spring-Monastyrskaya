package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.QuizResult;
import ru.otus.homework.service.abstractions.LocalizedIOService;
import ru.otus.homework.service.abstractions.ResultService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final LocalizedIOService ioService;

    @Override
    public void showResult(QuizResult quizResult) {
        ioService.printLine("");
        ioService.printLineLocalized("quiz.testResultHeader");
        ioService.printFormattedLineLocalized("quiz.student", quizResult.getStudent().getFullName());
        ioService.printFormattedLineLocalized("quiz.testResult", String.valueOf(quizResult.getRightAnswersCount()));

        if (quizResult.isTestPassed()) {
            ioService.printLineLocalized("quiz.passed");
            return;
        }
        ioService.printLineLocalized("quiz.failed");
    }
}
