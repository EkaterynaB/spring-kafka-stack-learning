package dev.danvega.kafka.basics;

import java.util.HashMap;
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
        int total = 0;

        for (Inbox inbox : boxes.values()) {
            total += inbox.getCount();
        }
        return total;
    }
}
