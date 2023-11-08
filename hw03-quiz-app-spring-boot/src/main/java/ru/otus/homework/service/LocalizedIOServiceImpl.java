package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.otus.homework.service.abstractions.IOService;
import ru.otus.homework.service.abstractions.LocalizedIOService;
import ru.otus.homework.service.abstractions.LocalizedMessagesService;

@Primary
@RequiredArgsConstructor
@Service
public class LocalizedIOServiceImpl implements IOService, LocalizedIOService {

    private final LocalizedMessagesService localizedMessagesService;

    private final IOService ioService;

    @Override
    public void printLine(String s) {
        ioService.printLine(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        ioService.printFormattedLine(s, args);
    }

    @Override
    public String readString() {
        return ioService.readString();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        return ioService.readStringWithPrompt(prompt);
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        return ioService.readIntForRange(min, max, errorMessage);
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        return ioService.readIntForRangeWithPrompt(min, max, prompt, errorMessage);
    }

    @Override
    public void printLineLocalized(String msgCode) {
        ioService.printLine(localizedMessagesService.getMessage(msgCode));
    }

    @Override
    public void printFormattedLineLocalized(String msgCode, Object... args) {
        ioService.printLine(localizedMessagesService.getMessage(msgCode, args));
    }

    @Override
    public String readStringWithPromptLocalized(String promptCode) {
        return ioService.readStringWithPrompt(localizedMessagesService.getMessage(promptCode));
    }

    @Override
    public int readIntForRangeLocalized(int min, int max, String errorMessageCode) {
        return ioService.readIntForRange(min, max, localizedMessagesService.getMessage(errorMessageCode));
    }

    @Override
    public int readIntForRangeWithPromptLocalized(int min, int max, String promptCode, String errorMessageCode) {
        return ioService.readIntForRangeWithPrompt(min, max,
                localizedMessagesService.getMessage(promptCode),
                localizedMessagesService.getMessage(errorMessageCode)
                );
    }

    @Override
    public String getMessage(String code, Object... args) {
        return localizedMessagesService.getMessage(code, args);
    }

    @Override
    public String getMessage(String msgCode) {
        return localizedMessagesService.getMessage(msgCode);
    }
}
