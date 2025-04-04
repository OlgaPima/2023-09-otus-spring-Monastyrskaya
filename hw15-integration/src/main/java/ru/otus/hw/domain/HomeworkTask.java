package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomeworkTask {
    private final int taskNumber;

    private final String theme;

    public String getTitle() {
        return "%s task №%s".formatted(theme, String.valueOf(taskNumber));
    }

    public boolean equals(HomeworkTask hw) {
        return this.taskNumber == hw.taskNumber && this.theme != null && this.theme.equalsIgnoreCase(hw.theme);
    }
}