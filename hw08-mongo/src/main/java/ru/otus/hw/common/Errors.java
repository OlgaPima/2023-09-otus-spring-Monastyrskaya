package ru.otus.hw.common;

import lombok.Getter;

public enum Errors {
    ENTITY_NOT_FOUND("Обращение к отсутствующим в БД данным, исправьте введенные данные: %s"),
    BOOK_NOT_FOUND("Не найдена книга с id=%s"),
    AUTHOR_NOT_FOUND("Не найден автор с id=%s"),
    GENRE_NOT_FOUND("Не найден жанр с id=%s"),
    COMMENT_NOT_FOUND("Не найден комментарий с id=%s");

    @Getter
    private final String message;

    Errors(String message) {
        this.message = message;
    }
}
