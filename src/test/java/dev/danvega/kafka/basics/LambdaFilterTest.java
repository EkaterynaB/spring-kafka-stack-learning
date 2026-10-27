package dev.danvega.kafka.basics;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaFilterTest {

    @Test
    public void testLambdaFilter() {
        MessageFilter filter =
                message -> message != null && !message.isBlank();

        assertTrue(filter.accepts("Hello"));
        assertFalse(filter.accepts("   "));
        assertFalse(filter.accepts(null));
    }

    @Test
    public void testLambdaFilterDoTheSameAsNotBlankFilter() {
        MessageFilter filter = new NotBlankFilter();
        MessageFilter lambdaFilter =
                message -> message != null && !message.isBlank();
        assertEquals(lambdaFilter.accepts("Hello"), filter.accepts("Hello"));
        assertEquals(lambdaFilter.accepts("   "), filter.accepts("   "));
        assertEquals(lambdaFilter.accepts(null), filter.accepts(null));
    }

    @Test
    public void testLambdaLimitFilter() {
        int limitNumber = 5;
        MessageFilter filter =
                message -> message != null && message.length() <= limitNumber;

        assertTrue(filter.accepts("Hello"));
        assertFalse(filter.accepts("Hello!"));
    }

    @Test
    public void testLambdaNotLinkFilter() {
        String bannedString = "http";
        MessageFilter filter =
                message -> message != null && !message.contains(bannedString);

        assertFalse(filter.accepts("linkt is here: https://google.com"));
        assertFalse(filter.accepts("https://google.com"));
        assertTrue(filter.accepts("Hello!"));
    }

    @Test
    public void checkReceivedMessages() {
        Inbox inboxWithFilters = new Inbox("Kate", List.of(message -> message != null && !message.isBlank(), new BannedWordFilter("spam")));
        inboxWithFilters.receive("Hello!");
        inboxWithFilters.receive("How are you?");
        inboxWithFilters.receive("  this is spam ");
        inboxWithFilters.receive("   ");
        inboxWithFilters.receive("See you tomorrow!");
        assertEquals(3, inboxWithFilters.getCount());
        assertEquals(List.of("Hello!", "How are you?", "See you tomorrow!"), inboxWithFilters.getMessages());
    }

    @Test
    public void testLambdaLimitChangeFilter() {
        int limitNumber = 5;
        MessageFilter filter =
                message -> message != null && message.length() <= limitNumber;
        // Variable used in lambda expression should be final or effectively final
        // limitNumber = 10;

        assertTrue(filter.accepts("Hello"));
        assertFalse(filter.accepts("Hello!"));
    }

    @Test
    public void testTwoFiltersOne() {
        MessageFilter strict = new NotBlankFilter().and(new BannedWordFilter("spam"));
        Inbox inboxWithFilters = new Inbox("Kate", List.of(strict));
        inboxWithFilters.receive("Hello!");
        inboxWithFilters.receive("How are you?");
        inboxWithFilters.receive("  this is spam ");
        inboxWithFilters.receive("See you tomorrow!");
        assertEquals(3, inboxWithFilters.getCount());
    }

    @Test
    public void testTwoFiltersTwo() {
        MessageFilter strict = new NotBlankFilter().and(new BannedWordFilter("spam"));
        assertTrue(strict.accepts("Hello"));
        assertFalse(strict.accepts("   "));
        assertFalse(strict.accepts("  this is spam  "));
    }
}
