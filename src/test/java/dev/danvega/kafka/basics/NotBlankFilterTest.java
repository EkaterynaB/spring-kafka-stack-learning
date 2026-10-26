package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotBlankFilterTest {
    @Test
    public void testValidDate() {
        MessageFilter filter = new NotBlankFilter();
        assertEquals(filter.accepts("Hello"), true);
    }
    @Test
    public void testInvalidDate() {
        MessageFilter filter = new NotBlankFilter();
        assertEquals(filter.accepts("    "), false);
        assertEquals(filter.accepts(null), false);
    }
}
