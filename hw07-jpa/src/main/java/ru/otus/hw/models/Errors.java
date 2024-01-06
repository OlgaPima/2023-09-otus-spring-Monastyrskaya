package ru.otus.hw.models;

import lombok.Getter;

public enum Errors {
    ENTITY_NOT_FOUND("Обращение к отсутствующим в БД данным, исправьте введенные данные: %s"),
    BOOK_SAVE_ERROR("Ошибка сохранения книги: %s"),
    BOOK_NOT_FOUND("Не найдена книга с id=%d"),
    COMMENT_SAVE_ERROR("Ошибка сохранения комментария: %s"),
    COMMENT_NOT_FOUND("Не найден комментарий с id=%d");

    @Getter
    private final String message;

    Errors(String message) {
        this.message = message;
    }
}
