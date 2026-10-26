package dev.danvega.kafka.basics;

import dev.danvega.kafka.greeting.Greeting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BannedWordFilterTest {
    @Test
    public void rejectsBannedWordRegardlessOfCase() {
        MessageFilter filter = new BannedWordFilter("spam");
        assertEquals(filter.accepts("SPAM bla bla"), false);
        assertEquals(filter.accepts("spaM bla bla"), false);
        assertEquals(filter.accepts("bla bla"), true);
    }

    @Test
    void acceptsRegularMessage() {
        MessageFilter filter = new BannedWordFilter("spam");

        assertTrue(filter.accepts("Hello, how are you?"));
    }

    @Test
    void shouldThrowWhenBannedWordIsBlank() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BannedWordFilter(null)
        );

        assertEquals(
                "Banned word must not be null or blank",
                exception.getMessage()
        );
    }
}
