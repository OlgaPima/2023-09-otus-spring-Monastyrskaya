package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Question;
import ru.otus.homework.repository.QuestionsRepository;
import ru.otus.homework.repository.dto.QuestionDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuestionsRepository questionsRepository;
    private final IOService ioService;

    @Override
    public void askQuestions() {
        List<Question> questions;
        try {
             questions = questionsRepository.readAll().stream().map(
                    dtoQuestion -> new Question(dtoQuestion.getOrderNum(), dtoQuestion.getQuestionText(), dtoQuestion.getAnswerText())
                ).collect(Collectors.toList());
        }
        catch (Exception e) {
            ioService.printLine("Csv file with questions is incorrect or missing. Ask your system administrator!");
            return;
        }

        ioService.printFormattedLine("Please answer the following %d questions:", questions.size());
        questions.stream().forEach(q -> System.out.println(q.getQuestionText()));
    }
}
