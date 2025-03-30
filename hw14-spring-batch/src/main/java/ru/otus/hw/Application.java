package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;


@SpringBootApplication
public class Application {

	public static void main(String[] args) throws SQLException {
//		Console.main(args); // включить, чтобы отобразилась консоль H2
		SpringApplication.run(Application.class, args);
	}

}
