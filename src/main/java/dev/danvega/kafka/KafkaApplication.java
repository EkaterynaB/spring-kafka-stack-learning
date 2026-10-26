package dev.danvega.kafka;

import dev.danvega.kafka.greeting.Greeting;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.json.JsonMapper;

@SpringBootApplication
public class KafkaApplication {
    private final JsonMapper jsonMapper;

    public KafkaApplication(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}

	// Spring Boot picks up any NewTopic beans and hands them to a KafkaAdmin, which on startup asks the broker to create them.
	// The admin client uses an idempotent "create if not exists" call under the hood, so if greetings already exists,
	// the broker just shrugs and moves on.
	@Bean
    NewTopic greetings() {
		return TopicBuilder.name("greetings")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
    ApplicationRunner runner(KafkaTemplate<String, String> template) {
		return args -> template.send("greetings", "Hello, Spring for Apache Kafka!");
	}

	@KafkaListener(topics = "greetings", groupId = "demo2")
	public void listen(String message) {
		System.out.println("got: " + message);
	}

    @Bean
    NewTopic myTestTopic() {
        return TopicBuilder.name("myTestTopic")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    ApplicationRunner runnerMyTestTopic(KafkaTemplate<String, String> template) {
        return args -> template.send("myTestTopic", "Well well well");
    }

    @KafkaListener(topics = "myTestTopic", groupId = "testDemo")
    public void listenMyTestTopic(String message) {
        System.out.println("got: " + message);
    }

    @Bean
    NewTopic greetingEventsTopic() {
        return TopicBuilder.name("greeting-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    ApplicationRunner runnerGreetingEvents(KafkaTemplate<String, String> template) {
        return args -> {
            Greeting greeting = Greeting.of("John", "Hello, beautiful!");
            String greetingOutput = jsonMapper.writeValueAsString(greeting);
            template.send("greeting-events", greetingOutput);
        };
    }

    @KafkaListener(topics = "greeting-events", groupId = "greetingEventId")
    public void listenGreetingEvents(String message) {
        Greeting greeting = jsonMapper.readValue(message, Greeting.class);
        System.out.println("greeting: " + greeting);
    }
}
