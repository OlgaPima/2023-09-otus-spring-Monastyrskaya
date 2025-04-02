package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.domain.ReadyTask;
import ru.otus.hw.services.StudentService;

@Configuration
public class IntegrationConfig {

	@Bean
	public MessageChannelSpec<?, ?> homeworkChannel() {
		return MessageChannels.queue(10);
	}

	@Bean
	public MessageChannelSpec<?, ?> readyTasksChannel() {
		return MessageChannels.publishSubscribe();
	}

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerSpec poller() {
		return Pollers.fixedRate(100).maxMessagesPerPoll(2);
	}

	@Bean
	public IntegrationFlow studentLearningFlow(StudentService studentService) {
		return IntegrationFlow.from(homeworkChannel())
				.split()
				.handle(studentService, "learn")
				.<ReadyTask, ReadyTask>transform(t -> new ReadyTask(t.getTaskNumber(), t.getTheme()))
				.aggregate()
				.channel(readyTasksChannel())
				.get();
	}
}
