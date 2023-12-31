package ru.otus.hw.commands;

import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Errors;

public abstract class SaveToDbCommands {
    // типа логируем красиво и универсально
    protected String saveChangesAndLog(SaveAction action, Errors errorMessageForException) {
        try {
            return action.execute();
        } catch (EntityNotFoundException e) {
            return Errors.ENTITY_NOT_FOUND.getMessage().formatted(e.getMessage());
        } catch (Exception e) {
            return errorMessageForException.getMessage().formatted(e.getMessage());
        }
    }
}
