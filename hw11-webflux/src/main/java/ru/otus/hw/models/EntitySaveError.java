package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для передачи на клиента (фронт) серверных ошибок: как ошибок валидации (имя поля + текст ошибки),
 * так и остальных серверных ошибок (не связанных с конкретным полем)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntitySaveError {
    private String fieldName;

    private String errorMessage;
}
