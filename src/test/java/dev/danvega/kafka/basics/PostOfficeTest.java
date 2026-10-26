package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;

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
}
