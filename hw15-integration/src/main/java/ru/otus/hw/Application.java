package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.services.UniversityService;

@Slf4j
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

		UniversityService universityService = ctx.getBean(UniversityService.class);
		universityService.generateHomeworksForStudent();
	}
}
