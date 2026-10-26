package dev.danvega.kafka.basics;

public interface MessageFilter {
    boolean accepts(String message);
}