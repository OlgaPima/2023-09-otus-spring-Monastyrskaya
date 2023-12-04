package ru.otus.hw.models;

import lombok.Getter;

public enum Errors {
    ENTITY_NOT_FOUND("Обращение к не существующим в БД данным, исправьте введенные данные: %s"),
    OTHER_ERROR("Ошибка сохранения книги: %s"),
    BOOK_NOT_FOUND("Не найдена книга с id=%d");

    @Getter
    private final String message;

    Errors(String message) {
        this.message = message;
    }
}
