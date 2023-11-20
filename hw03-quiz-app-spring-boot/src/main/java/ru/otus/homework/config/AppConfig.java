package ru.otus.homework.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
//@Component //по следам очень доооолгих раскопок. Если включить эту аннотацию, то появляются 2 бина: штатный appConfig
            // и второй application-ru.otus.homework.appConfig в "unknown location", и приложение не может стартануть - путается в них.
            // Итого: @ConfigurationProperties нельзя совмещать с @Component
@ConfigurationProperties(prefix = "application")
public class AppConfig implements LocaleConfig {

    private final int answersToPass;
    private final String locale;

    public AppConfig(int answersToPass, String locale) {
        this.answersToPass = answersToPass;
        this.locale = locale;
    }
}
