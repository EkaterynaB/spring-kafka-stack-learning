package dev.danvega.kafka.basics;

public class MaxLengthFilter implements MessageFilter {
    private final int maxLength;

    public MaxLengthFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean accepts(String message) {
        return message != null && message.length() <= maxLength;
    }
}
