package dev.danvega.kafka.greeting;

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GreetingTest {

    @Test
    void shouldThrowWhenFromIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Greeting.of(null, "message"));
        assertEquals(
                "From must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenFromIsBlank() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Greeting.of("   ", "Hello world")
        );

        assertEquals(
                "From must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenFromIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Greeting.of("", "Hello world")
        );

        assertEquals(
                "From must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenMessageIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Greeting.of("test user", null));
        assertEquals(
                "Message must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenMessageIsBlank() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Greeting.of("test user", " "));
        assertEquals(
                "Message must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldAcceptMessageWithExactly280Characters() {
        String message = "a".repeat(Greeting.MAX_MESSAGE_LENGTH);

        Greeting greeting = Greeting.of("test user", message);
        assertEquals(message, greeting.message());
    }

    @Test
    void shouldThrowWhenMessageMoreThan280Characters() {
        String message = "a".repeat(Greeting.MAX_MESSAGE_LENGTH + 1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Greeting.of("test user", message)
        );

        assertEquals(
                "Message cannot exceed " + Greeting.MAX_MESSAGE_LENGTH + " characters",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenSentAtIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Greeting("test user", "test message", null)
        );

        assertEquals(
                "sentAt must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateGreetingWithValidFields() {
        Greeting greeting = Greeting.of("test user", "Hello world");
        assertEquals("test user", greeting.from());
        assertEquals("Hello world", greeting.message());
        assertNotNull(greeting.sentAt());
    }

    @Test
    void shouldSerializeAndDeserializeGreeting() {
        // Arrange
        JsonMapper jsonMapper = new JsonMapper();
        Greeting original = Greeting.of("John", "Hello!");

        // Act
        String json = jsonMapper.writeValueAsString(original);
        Greeting restored = jsonMapper.readValue(json, Greeting.class);

        // Assert
        assertEquals(original, restored);
    }
}
