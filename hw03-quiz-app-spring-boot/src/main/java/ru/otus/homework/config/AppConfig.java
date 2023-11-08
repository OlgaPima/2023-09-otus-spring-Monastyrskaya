package ru.otus.homework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
//@EnableConfigurationProperties(AppConfig.class)
@ConfigurationProperties(prefix = "application")
public class AppConfig implements LocaleConfig {

    private int answersToPass;
    private String locale;

//    @ConstructorBinding //TODO: почему не работает такой конструктор? не видит аргументы answersToPass, locale
//    public AppConfig(int answersToPass, String locale) {
//        this.answersToPass = answersToPass;
//        this.locale = locale;
//    }
}
