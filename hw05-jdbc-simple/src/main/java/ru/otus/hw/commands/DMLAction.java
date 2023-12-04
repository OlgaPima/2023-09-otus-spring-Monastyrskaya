package ru.otus.hw.commands;

@FunctionalInterface
public interface DMLAction {
    String saveChangesToDB();
}
