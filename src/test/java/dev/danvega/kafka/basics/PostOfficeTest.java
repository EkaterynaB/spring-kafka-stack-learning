package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostOfficeTest {

    @Test
    public void testPostOfficeWithMultipleOwners() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("kate", "my first message");
        postOffice.deliver("john", "his first message");
        assertEquals(2, postOffice.totalMessages());
        Inbox inboxOne = postOffice.getInbox("kate");
        Inbox inboxTwo = postOffice.getInbox("kate");
        assertEquals(1, inboxOne.getCount());
        assertEquals(1, inboxTwo.getCount());
    }

    @Test
    public void testPostOfficeWithMultipleMessages() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("kate", "my first message");
        postOffice.deliver("kate", "my second message");
        postOffice.deliver("kate", "my three message");
        Inbox inbox = postOffice.getInbox("kate");
        assertEquals(3, inbox.getCount());
    }

    @Test
    public void testTotalMessages() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("kate", "my first message");
        postOffice.deliver("kate", "my second message");
        postOffice.deliver("John", "my second message");
        postOffice.deliver("John", "my three message");
        assertEquals(4, postOffice.totalMessages());
    }

    @Test
    public void returnNullIfOwnerDoesNotExist() {
        PostOffice postOffice = new PostOffice();
        assertNull(postOffice.getInbox("John"));
    }

    @Test
    public void returnDifferentInboxes() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("kate", "my first message");
        postOffice.deliver("John", "his first message");
        assertNotEquals(postOffice.getInbox("kate"), postOffice.getInbox("John"));
    }

    @Test
    public void deliverWithBlankMessageDoesNotCreateInbox() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("kate", "    ");
        assertNull(postOffice.getInbox("kate"));
    }

    @Test
    void deliverWithNullMessageDoesNotCreateInbox() {
        PostOffice postOffice = new PostOffice();

        postOffice.deliver("Kate", null);

        assertNull(postOffice.getInbox("Kate"));
        assertEquals(0, postOffice.totalMessages());
    }

    @Test
    public void deliverAllMessages() {
        PostOffice postOffice = new PostOffice();
        int rejectedCount = postOffice.deliverAll("Kate", List.of("Hello", "   ", "Kate"));
        assertEquals(1, rejectedCount);
        Inbox inbox = postOffice.getInbox("Kate");
        assertEquals(2, inbox.getCount());
    }

    @Test
    public void deliverAllInvalidMessages() {
        PostOffice postOffice = new PostOffice();
        int rejectedCount = postOffice.deliverAll("Kate", List.of("   ", "   ", "   "));
        assertEquals(3, rejectedCount);
        Inbox inbox = postOffice.getInbox("Kate");
        assertNull(inbox);
    }

    @Test
    public void notDeliverInvalidMessages() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("Kate", "Hello");
        assertEquals(1, postOffice.getInbox("Kate").getCount());
        postOffice.deliverAll("Kate", List.of("   "));
        assertEquals(1, postOffice.getInbox("Kate").getCount());
    }

    @Test
    void testArchivePositiveScenarios(@TempDir Path tempDir) throws IOException {
        PostOffice postOffice = new PostOffice();

        postOffice.deliver("Kate", "Hello");
        postOffice.deliver("Kate", "How are you?");
        Path file = tempDir.resolve("archive.txt");
        postOffice.archive("Kate", file);

        assertTrue(Files.exists(file));
        assertEquals(
                List.of("Hello", "How are you?"),
                Files.readAllLines(file)
        );
    }

    @Test
    void archiveThrowsWhenOwnerDoesNotExist(@TempDir Path tempDir) {
        PostOffice postOffice = new PostOffice();
        Path file = tempDir.resolve("archive.txt");

        ArchiveException exception = assertThrows(
                ArchiveException.class,
                () -> postOffice.archive("Unknown", file)
        );

        assertEquals(
                "Inbox not found for owner: Unknown",
                exception.getMessage()
        );

        assertNull(exception.getCause());
        assertFalse(Files.exists(file));
    }

    @Test
    void archiveWrapsIOExceptionWhenWritingFails(@TempDir Path tempDir) {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("Kate", "Hello");

        Path missingDirectory = tempDir.resolve("missing");
        Path file = missingDirectory.resolve("archive.txt");

        ArchiveException exception = assertThrows(
                ArchiveException.class,
                () -> postOffice.archive("Kate", file)
        );

        assertInstanceOf(IOException.class, exception.getCause());
        assertFalse(Files.exists(file));
    }

    @Test
    void testActiveOwners() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("Kate", "Hello");
        postOffice.deliver("Kate", "How are you?");
        postOffice.deliver("Anna", "hi");
        postOffice.deliver("Bob", "hey");
        assertEquals(List.of("Anna", "Bob", "Kate"), postOffice.activeOwners());
    }

    @Test
    void anyInboxContainsWords() {
        PostOffice postOffice = new PostOffice();
        postOffice.deliver("Kate", "Hello");
        postOffice.deliver("Kate", "How are you?");
        postOffice.deliver("Bob", "What is your name?");
        Inbox inbox = postOffice.getInbox("Kate");
        assertEquals(List.of("Hello", "How are you?"), inbox.getMessages());
        assertTrue(postOffice.anyInboxContains("Hello"));
        assertTrue(postOffice.anyInboxContains("hello"));
        assertTrue(postOffice.anyInboxContains("name"));
        assertFalse(postOffice.anyInboxContains("names"));
        assertFalse(postOffice.anyInboxContains(null));
    }
}
