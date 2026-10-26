package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaxLengthFilterTest {
    @Test
    void acceptCorrectValue() {
        MessageFilter filter = new MaxLengthFilter(10);
        assertEquals(filter.accepts("Hello"), true);
    }

    @Test
    void acceptIncorrectValue() {
        MessageFilter filter = new MaxLengthFilter(5);
        assertEquals(filter.accepts("Hello"), true);
        assertEquals(filter.accepts("Hello!"), false);
        assertEquals(filter.accepts(null), false);
    }

    @Test
    void acceptsMessageExactlyAtMaxLength() {
        MessageFilter filter = new MaxLengthFilter(6);

        assertTrue(filter.accepts("Hello!"));
    }
}
