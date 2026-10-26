package dev.danvega.kafka.greeting;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public record Greeting(String from, String message, Instant sentAt ) {

    public static final int MAX_MESSAGE_LENGTH = 280;

    public Greeting {
        if (from == null || from.isBlank()) {
            throw new IllegalArgumentException("From must not be null or blank");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message must not be null or blank");
        }
        int characterCount = message.codePointCount(
                0,
                message.length()
        );

        if (characterCount > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Message cannot exceed " + MAX_MESSAGE_LENGTH + " characters");
        }

        if (sentAt == null) {
            throw new IllegalArgumentException("sentAt must not be null");
        }
    }

    public static Greeting of(String from, String message) {
        return new Greeting(from, message, Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
