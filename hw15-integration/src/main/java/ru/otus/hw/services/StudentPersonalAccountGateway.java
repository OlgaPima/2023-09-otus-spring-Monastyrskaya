package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.HomeworkTask;
import ru.otus.hw.domain.ReadyTask;

import java.util.List;

@MessagingGateway
public interface StudentPersonalAccountGateway {
    @Gateway(requestChannel = "homeworkChannel", replyChannel = "readyTasksChannel")
    List<ReadyTask> process(List<HomeworkTask> homeworkTasks);
}