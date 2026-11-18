package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriorityTest {
    @Test
    void shouldClassifyMessagePriority() {
        assertEquals(Priority.HIGH, Priority.classify("URGENT: fix this"));
        assertEquals(Priority.LOW, Priority.classify("Just FYI"));
        assertEquals(Priority.LOW, Priority.classify("Just fyi"));
        assertEquals(Priority.NORMAL, Priority.classify("Hello"));
        assertEquals(Priority.NORMAL, Priority.classify(null));
        assertEquals(Priority.HIGH, Priority.classify("FYI: urgent issue"));
    }

    @Test
    void shouldResponseHoursReturnValue() {
        assertEquals(1, Priority.HIGH.responseHours());
        assertEquals(24, Priority.NORMAL.responseHours());
        assertEquals(72, Priority.LOW.responseHours());
        String a = "HIGH";
        String b = "HIGH";
        System.out.println(a == b);
    }
}
