package dev.danvega.kafka.basics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostOffice {
    private final Map<String,Inbox> boxes = new HashMap<>();

    public void deliver(String owner, String message) {
        Inbox inboxNew = new Inbox(owner);
        if (!inboxNew.isValidMessage(message)) { return;}

        Inbox inbox = getInbox(owner);

        if (inbox == null) {
            inbox = new Inbox(owner);
            boxes.put(owner, inbox);
        }
        inbox.receive(message);
    }

    public Inbox getInbox(String owner) {
        return boxes.get(owner);
    }

    public int totalMessages() {
        return boxes.values().stream()
            .mapToInt(Inbox::getCount)
            .sum();
    }

    
    public void archive(String owner, Path filePath) throws ArchiveException {
        Inbox inbox = getInbox(owner);
        // owner does not exist
        if (inbox == null) {
            throw new ArchiveException("Inbox not found for owner: " + owner);
        }

        // without try catch I get this error - Unhandled exception: java.io.IOException
        // without catch java: unreported exception java.io.IOException; must be caught or declared to be thrown
        try {
            List<String> messages = inbox.getMessages();
            Files.write(filePath, messages);
        }
        catch (IOException e) {
            throw new ArchiveException(e.getMessage(), e);
        }

    }

    public int deliverAll(String owner, List<String> messages) {
        int rejectedCount = 0;
        for (String message : messages) {
            try {
                Inbox inbox = getInbox(owner);
                boolean isNewInbox = inbox == null;

                if (isNewInbox) {
                    inbox = new Inbox(owner);
                }
                inbox.receiveStrict(message);
                if (isNewInbox) {
                    boxes.put(owner, inbox);
                }
            } catch (InvalidMessageException e) {
                rejectedCount += 1;
            }
        }

        return rejectedCount;
    }

    public List<String> activeOwners() {
        return boxes.entrySet()
            .stream()
            .filter(entry -> entry.getValue().getCount() > 0)
            .map(Map.Entry::getKey)
            .sorted()
            .toList();
    }

    public boolean anyInboxContains(String word) {
        return boxes.values()
            .stream()
            .anyMatch(inbox -> !inbox.search(word).isEmpty());
    }
}
