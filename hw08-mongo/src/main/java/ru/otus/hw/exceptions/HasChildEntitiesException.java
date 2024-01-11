package ru.otus.hw.exceptions;

public class HasChildEntitiesException extends RuntimeException {
    public HasChildEntitiesException(String message) {
        super(message);
    }
}
