package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.homework.service.abstractions.TestRunnerService;

@SpringBootApplication
//@EnableConfigurationProperties(QuizConfig.class)
public class Main {

	public static void main(String[] args) {
		//Создаем контекст Spring Boot приложения
		ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

		// запускаем опросник
		var testRunnerService = context.getBean(TestRunnerService.class);
		testRunnerService.run();
	}

}
