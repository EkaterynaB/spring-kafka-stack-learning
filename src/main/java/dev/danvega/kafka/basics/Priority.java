package dev.danvega.kafka.basics;

import java.util.Objects;

public enum Priority {
    LOW(1),
    NORMAL(2),
    // CRITICAL(3), // Adding a new enum value causes a compilation error if the switch
    //  doesn't handle it. A default case prevents this error, but makes
    //  it easier to miss newly added enum values.
    HIGH(3);

    private final int weight;

    Priority(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public static Priority classify(String message) {
        if (message == null) {
            return NORMAL;
        }

        String text = message.toLowerCase();

        if (text.contains("urgent")) {
            return HIGH;
        }

        if (text.contains("fyi")) {
            return LOW;
        }

        return NORMAL;
    }

    public int responseHours() {
        return switch (this) {
            case HIGH -> 1;
            case NORMAL -> 24;
            case LOW -> 72;
        };
    }
}
