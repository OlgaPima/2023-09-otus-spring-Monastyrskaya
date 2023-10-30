package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.QuizConfig;
import ru.otus.homework.model.Question;
import ru.otus.homework.model.QuizResult;
import ru.otus.homework.model.Student;
import ru.otus.homework.repository.QuestionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuestionsRepository questionsRepository;
    private final IOService ioService;
    private final QuizConfig quizConfig;

    @Override
    public QuizResult executeTestFor(Student student) {
        var questions = getAllQuestions();

        if (questions != null) {
            ioService.printLine("");
            ioService.printFormattedLine("Please answer the questions below%n");

            var quizResult = new QuizResult(student, quizConfig);
            for (var question : questions) {
                String answer = ioService.readStringWithPrompt(String.format("%d. %s", question.getOrderNum(), question.getQuestionText()));
                quizResult.addAnswer(question, answer);
                ioService.printLine(question.isAnswerRight(answer) ? "yes!!!" : "no...");
            }
            return quizResult;
        }
        return null;
    }

    @Override
    public void askQuestions() {
        List<Question> questions = getAllQuestions();
        ioService.printFormattedLine("Please answer the following %d questions:", questions.size());
        questions.stream().forEach(q -> System.out.println(q.getQuestionText()));
    }

    private List<Question> getAllQuestions() {
        try {
            return questionsRepository.findAll().stream().map(
                    dtoQuestion -> new Question(dtoQuestion.getOrderNum(), dtoQuestion.getQuestionText(), dtoQuestion.getAnswerText())
            ).collect(Collectors.toList());
        } catch (Exception e) {
            ioService.printLine("Question file is incorrect or missing. Ask your system administrator. " + e.getMessage());
            return null;
        }
    }
}
