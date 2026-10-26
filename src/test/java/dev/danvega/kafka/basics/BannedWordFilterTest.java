package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BannedWordFilterTest {
    @Test
    public void rejectsBannedWordRegardlessOfCase() {
        MessageFilter filter = new BannedWordFilter("spam");
        assertEquals(filter.accepts("SPAM bla bla"), false);
        assertEquals(filter.accepts("spaM bla bla"), false);
    }

    @Test
    void acceptsRegularMessage() {
        MessageFilter filter = new BannedWordFilter("spam");

        assertTrue(filter.accepts("Hello, how are you?"));
    }

    @Test
    void shouldThrowWhenBannedWordIsBlank() {
        IllegalArgumentException exceptionOne = assertThrows(
                IllegalArgumentException.class,
                () -> new BannedWordFilter(null)
        );

        assertEquals(
                "Banned word must not be null or blank",
                exceptionOne.getMessage()
        );

        IllegalArgumentException exceptionTwo = assertThrows(
                IllegalArgumentException.class,
                () -> new BannedWordFilter("   ")
        );

        assertEquals(
                "Banned word must not be null or blank",
                exceptionTwo.getMessage()
        );
    }
}
