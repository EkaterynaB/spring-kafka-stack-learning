package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StreamTest {
    @Test
    void toListShouldBeUnmodifiable() {
        List<String> names = Stream.of("Alice", "Bob", "Kate")
                .toList();

        assertThrows(
                UnsupportedOperationException.class,
                () -> names.add("John")
        );

        assertEquals(3, names.size());
    }

    @Test
    void streamCannotBeReusedAfterTerminalOperation() {
        Stream<String> stream = Stream.of("Alice", "Bob", "Kate");

        List<String> result = stream.toList();

        assertEquals(List.of("Alice", "Bob", "Kate"), result);

        // A Stream cannot be reused after a terminal operation.
        // Calling count() again throws IllegalStateException.
        assertThrows(IllegalStateException.class, stream::count);
    }
}
