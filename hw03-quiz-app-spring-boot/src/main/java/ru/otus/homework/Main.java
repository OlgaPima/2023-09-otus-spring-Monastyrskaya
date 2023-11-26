package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import ru.otus.homework.config.AppConfig;
import ru.otus.homework.config.FileNameConfig;
import ru.otus.homework.service.abstractions.TestRunnerService;

@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class, FileNameConfig.class})
@PropertySource("classpath:application.yml")
public class Main {

	public static void main(String[] args) {
		//Создаем контекст Spring Boot приложения
		ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

		// запускаем опросник
		var testRunnerService = context.getBean(TestRunnerService.class);
		testRunnerService.run();
	}

}
