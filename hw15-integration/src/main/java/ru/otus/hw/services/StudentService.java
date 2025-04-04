package ru.otus.hw.services;

import ru.otus.hw.domain.HomeworkTask;
import ru.otus.hw.domain.ReadyTask;

public interface StudentService {
    ReadyTask learn(HomeworkTask homeworkTask);
}
