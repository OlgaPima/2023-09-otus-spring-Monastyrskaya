package ru.otus.homework.exception;

public class CsvParsingException extends RuntimeException {
    public CsvParsingException(String message) {
        super(message);
    }
}
