package ru.otus.homework.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTest {

    @Test
    @DisplayName("Проверка полного имени")
    void getFullName() {
        var student = new Student("Petr", "Vasechkin");
        assertEquals("Petr Vasechkin", student.getFullName());
    }
}