package ru.otus.hw.commands;

/**
 * Действие, которое нужно выполнить в БД (insert/update/delete).
 * Возвращает строку (результат действия), которую нужно отобразить в консоли
 */
@FunctionalInterface
public interface SaveAction {
    String execute();
}
