package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.AppConfig;
import ru.otus.homework.model.Question;
import ru.otus.homework.model.QuizResult;
import ru.otus.homework.model.Student;
import ru.otus.homework.repository.QuestionsRepository;
import ru.otus.homework.service.abstractions.LocalizedIOService;
import ru.otus.homework.service.abstractions.QuizService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuestionsRepository questionsRepository;
    private final AppConfig appConfig;
    private final LocalizedIOService ioService;

    @Override
    public QuizResult executeTestFor(Student student) {
        var questions = getAllQuestions();

        if (questions != null) {
            ioService.printLine("");
            ioService.printLineLocalized("quiz.beforeStart");

            var quizResult = new QuizResult(student, appConfig.getAnswersToPass());
            for (var question : questions) {
                String answer = ioService.readStringWithPrompt(String.format("%d. %s", question.getOrderNum(), question.getQuestionText()));
                quizResult.addAnswer(question, answer);
                String answerMsg = question.isAnswerRight(answer) ? ioService.getMessage("questions.rightAnswer")
                        : ioService.getMessage("questions.error");
                ioService.printLine(answerMsg);
            }
            return quizResult;
        }
        return null;
    }

    @Override
    public List<Question> getAllQuestions() {
        try {
            return questionsRepository.findAll().stream().map(
                    dtoQuestion -> new Question(dtoQuestion.getOrderNum(), dtoQuestion.getQuestionText(), dtoQuestion.getAnswerText())
            ).collect(Collectors.toList());
        } catch (Exception e) {
            ioService.printFormattedLineLocalized("quiz.fileReadingError", e.getMessage());
            return null;
        }
    }
}
