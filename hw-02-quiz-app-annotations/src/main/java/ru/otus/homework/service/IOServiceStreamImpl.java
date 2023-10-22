package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import java.io.PrintStream;

@Service
public class IOServiceStreamImpl implements IOService {
    private final PrintStream printStream = java.lang.System.out;

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }
}
