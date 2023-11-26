package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import ru.otus.homework.config.AppConfig;
import ru.otus.homework.config.FileNameConfig;

@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class, FileNameConfig.class})
@PropertySource("classpath:application.yml")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
