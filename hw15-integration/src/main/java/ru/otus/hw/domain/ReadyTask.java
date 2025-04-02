package ru.otus.hw.domain;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

@Getter
public class ReadyTask {
    private final int taskNumber;

    private final String theme;

    private final int quality;

    public ReadyTask(int taskNumber, String theme) {
        this.taskNumber = taskNumber;
        this.theme = theme;
        // качество выполненного задания от 1 до 10 (верхняя граница исключается)
        this.quality = RandomUtils.nextInt(1, 11);
    }

    public String getDescription() {
        return "%s task №%s - quality %s".formatted(theme, String.valueOf(taskNumber), String.valueOf(quality));
    }
}
