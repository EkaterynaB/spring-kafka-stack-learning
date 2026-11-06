package dev.danvega.kafka.basics;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class BasicsPlayground {

    static void main(String[] args) {
        Inbox kateInbox = new Inbox("Kate");
        Inbox johnInbox = new Inbox("John");

        kateInbox.receive("Hello!");
        kateInbox.receive("How are you?");
        kateInbox.receive("See you tomorrow!");

        johnInbox.receive("Good morning!");
        johnInbox.receive("     ");

//        System.out.println(kateInbox.toString());

//        System.out.println();

//        System.out.println(johnInbox);

//        System.out.println(" ----- ");
//        System.out.println(kateInbox.hashCode());
//        System.out.println(" ???????? ");
//        System.out.println(johnInbox.hashCode());

//        kateInbox.getMessages().add("hack");
//        System.out.println(kateInbox.getCount());

        Inbox inboxWithFilters = new Inbox("Kate", List.of((MessageFilter) message -> message != null && !message.isBlank(), new BannedWordFilter("spam")));
        inboxWithFilters.receive("Hello!");
        inboxWithFilters.receive("How are you?");
        inboxWithFilters.receive("  this is spam ");
        inboxWithFilters.receive("   ");
        inboxWithFilters.receive("See you tomorrow!");
//        System.out.println(inboxWithFilters.getCount());

        PostOffice postOffice = new PostOffice();

        postOffice.deliver("Kate", "Hello");
        postOffice.deliver("Kate", "Learning Java");
        postOffice.deliver("Kate", "Kafka is interesting");

//        postOffice.archive("Kate", Path.of("kate-messages-test.txt"));

//        will be an empty output since filter is intermediate operation
        Stream<String> stream = Stream.of("Alice", "Bob", "Kate")
                .filter(name -> {
                    System.out.println("Checking: " + name);
                    return name.length() > 3;
                });

        // because toList terminal operation
        List<String> names = Stream.of("Alice", "Bob", "Kate")
                .filter(name -> {
                    System.out.println("Checking: " + name);
                    return name.length() > 3;
                })
                .toList();

        System.out.println(names);
    }
}
