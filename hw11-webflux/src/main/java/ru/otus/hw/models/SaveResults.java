package ru.otus.hw.models;

import lombok.Getter;

public enum SaveResults {
    SUCCESS("success"),
    ERROR("error");

    @Getter
    private final String name;

    SaveResults(String name) {
        this.name = name;
    }
}
