package ru.otus.hw.commands;

import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.common.Errors;

public abstract class AbstractSaveCommands {
    // типа логируем красиво и универсально
    protected String saveChangesAndLog(SaveAction action) {
        try {
            return action.execute();
        } catch (EntityNotFoundException e) {
            return Errors.ENTITY_NOT_FOUND.getMessage().formatted(e.getMessage());
        } catch (Exception e) {
            return "Ошибка: %s".formatted(e.getMessage());
        }
    }
}
