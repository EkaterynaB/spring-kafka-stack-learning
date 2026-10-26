package dev.danvega.kafka.basics;

public class NotBlankFilter implements MessageFilter {
    @Override
    public boolean accepts(String message) {
        return message != null && !message.isBlank();
    }
}
