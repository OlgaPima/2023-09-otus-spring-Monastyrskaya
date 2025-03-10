package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntitySaveResult<T> {
    private String status;

    private T entityDto;

    private List<EntitySaveError> errors;

    public EntitySaveResult(BindingResult result) {
        this.errors = result.getFieldErrors().stream().map(fieldError ->
                            new EntitySaveError(fieldError.getField(), fieldError.getDefaultMessage())
                        ).collect(Collectors.toList());
        this.status = "error";
    }
}
