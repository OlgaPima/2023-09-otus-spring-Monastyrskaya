package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.LocaleConfig;
import ru.otus.homework.service.abstractions.LocalizedMessagesService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleConfig localeConfig;
    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object... args) {
        var locale = Locale.forLanguageTag(localeConfig.getLocale());
        if (args == null) {
            return messageSource.getMessage(code, null, locale);
        }
        else {
            return messageSource.getMessage(code, args, locale);
        }
    }

    @Override
    public String getMessage(String code) {
        return getMessage(code, null);
    }
}
