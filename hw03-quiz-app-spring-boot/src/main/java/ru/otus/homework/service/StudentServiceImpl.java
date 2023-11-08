package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Student;
import ru.otus.homework.service.abstractions.IOService;
import ru.otus.homework.service.abstractions.LocalizedIOService;
import ru.otus.homework.service.abstractions.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final LocalizedIOService ioService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPromptLocalized("student.inputFirstName");
        var lastName = ioService.readStringWithPromptLocalized("student.inputLastName");
        return new Student(firstName, lastName);
    }
}
