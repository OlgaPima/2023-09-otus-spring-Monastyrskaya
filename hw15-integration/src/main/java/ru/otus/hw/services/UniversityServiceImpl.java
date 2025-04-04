package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.ReadyTask;
import ru.otus.hw.domain.HomeworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private static final String[] THEMES = { "java", "spring", "sql", "html", "css", "C#", "ASP.NET" };

    private static final int TASKS_IN_HOMEWORK = 2;

    private final StudentPersonalAccountGateway studentAccountGateway;

    @Override
    public void generateHomeworksForStudent() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < THEMES.length; i++) {
            int num = i + 1;
            String currentTheme = THEMES[i];
            pool.execute(() -> {
                List<HomeworkTask> homework = generateHomeworks(currentTheme);
                log.info("{}, New homework: {}", num,
                        homework.stream().map(HomeworkTask::getTitle).collect(Collectors.joining(", ")));

                List<ReadyTask> readyTasks = studentAccountGateway.process(homework);
                log.info("{}, Ready task: {}", num,
                        readyTasks.stream().map(ReadyTask::getDescription).collect(Collectors.joining(", ")));
            });
            delay();
        }
    }

    private List<HomeworkTask> generateHomeworks(String theme) {
        List<HomeworkTask> homework = new ArrayList<>();
        for (int i = 0; i < TASKS_IN_HOMEWORK; ++i) {
            homework.add(new HomeworkTask(i + 1, theme));
        }
        return homework;
    }

    private void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
