package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class HasChildEntitiesException extends RuntimeException {
    private String deletingId;

    public HasChildEntitiesException(String message) {
        super(message);
    }

    public HasChildEntitiesException(String id, String message) {
        super(message);
        this.deletingId = id;
    }
}
