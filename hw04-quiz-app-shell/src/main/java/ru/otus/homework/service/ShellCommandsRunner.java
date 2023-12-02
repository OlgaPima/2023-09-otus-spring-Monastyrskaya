package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.model.Student;
import ru.otus.homework.service.abstractions.TestRunnerService;

@ShellComponent
public class ShellCommandsRunner {
    private String firstName;
    private Student student;

    @Autowired
    private TestRunnerService testRunnerService;

    @ShellMethod(value = "Введите ваше имя", key = {"fn", "firstName"})
    public void firstName(@ShellOption(defaultValue = "Olga") String firstName) {
        this.firstName = firstName;
    }

    @ShellMethod(value = "Введите фамилию", key = {"ln", "lastName"})
    @ShellMethodAvailability(value = "isLastNameCommandAvailable")
    public void lastName(@ShellOption(defaultValue = "Mon") String lastName) {
        student = new Student(firstName, lastName);
    }

    @ShellMethod(key = {"start"})
    @ShellMethodAvailability(value = "isStartCommandAvailable")
    public void start() {
        testRunnerService.run(student);
    }

    private Availability isLastNameCommandAvailable() {
        return firstName == null? Availability.unavailable("Ввод фамилии недоступен: сначала введите свое имя")
                : Availability.available();
    }

    private Availability isStartCommandAvailable() {
        return student == null? Availability.unavailable("Тестирование недоступно: сначала введите свое имя")
                : Availability.available();
    }
}
