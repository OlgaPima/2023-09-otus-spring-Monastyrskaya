package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        // Для проверки отключить initBooks() в InitMongoDBDataChangeLog (Changeset "003")
        if (bookRepository.findAll().isEmpty()) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Диверсия! Враги украли все книжки!").build();
        } else {
            return Health.up().
                    status(Status.UP)
                    .withDetail("message", "Все хорошо, принимаем читателей").build();
        }
    }
}
