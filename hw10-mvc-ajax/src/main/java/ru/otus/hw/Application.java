package ru.otus.hw;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;


@SpringBootApplication
public class Application {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Application.class, args);
//		Console.main(args);
		System.out.printf("Чтобы перейти на страницу сайта, открывай: %n%s%n",
				"http://localhost:8080/authors");
	}
}
