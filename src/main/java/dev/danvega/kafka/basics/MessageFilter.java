package dev.danvega.kafka.basics;

@FunctionalInterface
public interface MessageFilter {
    boolean accepts(String message);

    default MessageFilter and(MessageFilter other) {
        return message -> accepts(message) && other.accepts(message);
    }
}