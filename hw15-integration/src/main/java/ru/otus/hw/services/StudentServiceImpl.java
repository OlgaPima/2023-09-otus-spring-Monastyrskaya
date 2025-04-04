package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.ReadyTask;
import ru.otus.hw.domain.HomeworkTask;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public ReadyTask learn(HomeworkTask homeworkTask) {
        log.info("Learning - {}", homeworkTask.getTitle());
        delay();
        log.info("Learning {} done", homeworkTask.getTitle());
        return new ReadyTask(homeworkTask.getTaskNumber(), homeworkTask.getTheme());
    }

    private void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
