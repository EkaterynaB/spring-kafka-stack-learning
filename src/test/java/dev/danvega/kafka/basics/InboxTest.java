package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InboxTest {

    @Test
    void newInboxHasCorrectOwner() {
        Inbox inbox = new Inbox("Kate");

        assertEquals("Kate", inbox.getOwner());
    }

    @Test
    void newInboxHasEmptyState() {
        Inbox inbox = new Inbox("Kate");

        assertEquals(0, inbox.getCount());
        assertNull(inbox.getLastMessage());
    }

    @Test
    void sameOwnerMeansEqual() {
        Inbox inbox1 = new Inbox("Kate");
        Inbox inbox2 = new Inbox("Kate");

        assertEquals(inbox1, inbox2);
    }

    @Test
    void hashCodeDoesNotChangeAfterReceive() {
        Inbox inbox = new Inbox("Kate");

        int originalHashCode = inbox.hashCode();

        inbox.receive("Hello");

        assertEquals(originalHashCode, inbox.hashCode());
    }

    @Test
    void receiveIncreasesCount() {
        Inbox inbox = new Inbox("Kate");
        inbox.receive("Hello");
        assertEquals(1, inbox.getCount());
        assertEquals("Hello", inbox.getLastMessage());
    }

    @Test
    void receiveMultipleMessages() {
        Inbox inbox = new Inbox("Kate");
        inbox.receive("Hello");
        inbox.receive("How are you?");
        inbox.receive("Goodbye");
        assertEquals(3, inbox.getCount());
        assertEquals("Goodbye", inbox.getLastMessage());
    }

    @Test
    void receiveNullDoesNothing() {
        Inbox inbox = new Inbox("Kate");
        inbox.receive("Hello");
        inbox.receive("How are you?");
        inbox.receive("Goodbye");
        inbox.receive(null);
        inbox.receive("   ");
        assertEquals(3, inbox.getCount());
    }

    @Test
    void receiveKeepsPreviousMessage() {
        Inbox inbox = new Inbox("Kate");
        inbox.receive("Hello");
        inbox.receive(null);
        inbox.receive("   ");
        assertEquals("Hello", inbox.getLastMessage());
    }

    @Test
    void oneOwnersDifferentNumberOfMessages() {
        Inbox inboxOne = new Inbox("Kate");
        Inbox inboxTwo = new Inbox("Kate");
        inboxOne.receive("Hello");
        inboxOne.receive("How are you?");
        inboxTwo.receive("Well well well");
        assertEquals(inboxOne, inboxTwo);
    }

    @Test
    void differentOwners() {
        Inbox inboxOne = new Inbox("Kate");
        Inbox inboxTwo = new Inbox("John");
        assertNotEquals(inboxOne, inboxTwo);
    }

    @Test
    void equalHashCode() {
        Inbox inboxOne = new Inbox("Kate");
        Inbox inboxTwo = new Inbox("Kate");
        assertEquals(inboxOne.hashCode(), inboxTwo.hashCode());
    }

    @Test
    void equalsReturnsFalseForNull() {
        Inbox inboxOne = new Inbox("Kate");
        assertFalse(inboxOne.equals(null));
    }

    @Test
    void equalsReturnsFalseForString() {
        Inbox inboxOne = new Inbox("Kate");
        assertFalse(inboxOne.equals("Kate"));
    }

    @Test
    void getMessagesReturnsAllMessagesInOrder() {
        Inbox inbox = new Inbox("Kate");

        inbox.receive("Hello");
        inbox.receive("How are you?");
        inbox.receive("Goodbye");

        assertEquals(
                List.of("Hello", "How are you?", "Goodbye"),
                inbox.getMessages()
        );
    }

    @Test
    void newInboxHasEmptyMessages() {
        Inbox inbox = new Inbox("Kate");

        assertNotNull(inbox.getMessages());
        assertTrue(inbox.getMessages().isEmpty());
    }

    @Test
    void getMessagesCannotBeModifiedExternally() {
        Inbox inbox = new Inbox("Kate");
        inbox.receive("Hello");

        List<String> messages = inbox.getMessages();

        assertThrows(
                UnsupportedOperationException.class,
                () -> messages.add("Hacked")
        );

        assertEquals(List.of("Hello"), inbox.getMessages());
    }

    @Test
    void inboxWithMultipleFiltersKeepsOnlyMessagesAcceptedByAll() {
        Inbox inbox = new Inbox("Kate", List.of(
                new NotBlankFilter(),
                new MaxLengthFilter(10),
                new BannedWordFilter("spam")
        ));

        inbox.receive("Hello");
        inbox.receive("   ");
        inbox.receive("This message is far too long");
        inbox.receive("Buy SPAM");
        inbox.receive(null);
        inbox.receive("Bye");

        assertEquals(List.of("Hello", "Bye"), inbox.getMessages());
    }
}
