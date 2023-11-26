package ru.otus.homework.service.abstractions;

public interface LocalizedMessagesService {
    String getMessage(String code, Object... args);
    String getMessage(String code);
}
