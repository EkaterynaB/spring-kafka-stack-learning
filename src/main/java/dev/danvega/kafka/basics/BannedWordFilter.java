package dev.danvega.kafka.basics;

public class BannedWordFilter implements MessageFilter {
    private final String bannedWord;

    public BannedWordFilter(String bannedWord) {
        if (bannedWord == null || bannedWord.isBlank()) {
            throw new IllegalArgumentException("Banned word must not be null or blank");
        }
        this.bannedWord = bannedWord.toLowerCase();
    }

    @Override
    public boolean accepts(String message) {
        return message != null && !message.toLowerCase().contains(bannedWord);
    }
}
