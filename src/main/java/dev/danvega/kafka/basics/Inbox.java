package dev.danvega.kafka.basics;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public class Inbox {
    private final String owner;
    private final List<String> messages = new ArrayList<>();
    private final List<MessageFilter> filters;

    public Inbox(String owner) {
        this(owner, List.of(new NotBlankFilter()));
    }

    public Inbox(String owner, List<MessageFilter> filters) {
        this.owner = owner;
        this.filters = List.copyOf(filters);
    }

    public void receive(String message) {
        if (!isValidMessage(message)) {
            return;
        }

        messages.add(message);
    }

    public void receiveStrict(String message) {
        MessageFilter rejectedBy = findRejectingFilter(message);
        if (rejectedBy != null) {
            throw new InvalidMessageException("Message rejected by: " + rejectedBy.getClass().getSimpleName());
        }
        messages.add(message);
    }

    public String getOwner() {
        return owner;
    }

    public int getCount() {
        return messages.size();
    }

    public String getLastMessage() {
        if (messages.isEmpty()) {return null;}
        return messages.getLast();
    }

    public List<String> getMessages() {
        return List.copyOf(messages);
    }

    @Override
    public String toString() {
        return "Owner: " + owner + "; Count:  " + getCount() + "; Last message:  " + getLastMessage();
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Inbox inbox)) return false;
        return Objects.equals(owner, inbox.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(owner);
    }

    private MessageFilter findRejectingFilter(String message) {
        for (MessageFilter filter : filters) {
            if (!filter.accepts(message)) {
                return filter;
            }
        }

        return null;
    }

    public boolean isValidMessage(String message) {
        return findRejectingFilter(message) == null;
    }
}
