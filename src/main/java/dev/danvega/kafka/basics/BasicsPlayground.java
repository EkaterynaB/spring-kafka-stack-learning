package dev.danvega.kafka.basics;

public class BasicsPlayground {

    static void main(String[] args) {
        Inbox kateInbox = new Inbox("Kate");
        Inbox johnInbox = new Inbox("John");

        kateInbox.receive("Hello!");
        kateInbox.receive("How are you?");
        kateInbox.receive("See you tomorrow!");

        johnInbox.receive("Good morning!");
        johnInbox.receive("     ");

        System.out.println(kateInbox.toString());

        System.out.println();

        System.out.println(johnInbox);

        System.out.println(" ----- ");
//        System.out.println(kateInbox.hashCode());
        System.out.println(" ???????? ");
//        System.out.println(johnInbox.hashCode());

        kateInbox.getMessages().add("hack");
        System.out.println(kateInbox.getCount());
    }
}
