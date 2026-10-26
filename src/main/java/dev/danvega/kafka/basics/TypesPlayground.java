package dev.danvega.kafka.basics;

public class TypesPlayground {

    static void main(String[] args) {
        int a = 1000;          int b = 1000;          // a == b
        Integer c = 1000;      Integer d = 1000;      // c == d       та  c.equals(d)
        Integer e = 100;       Integer f = 100;       // e == f
        String s1 = "hello";   String s2 = "hello";   // s1 == s2
        String s3 = new String("hello");              // s1 == s3     та  s1.equals(s3)
        Inbox x = new Inbox("Kate");
        Inbox y = new Inbox("Kate");                  // x == y       та  x.equals(y)
        System.out.println(x);                        // що виведеться?
        System.out.println("a == b true ");
        System.out.println(a == b);
        System.out.println("c == d false ");
        System.out.println(c == d);
        System.out.println("c.equals(d) true ");
        System.out.println(c.equals(d));
        System.out.println("e == f true ");
        System.out.println(e == f);
        System.out.println("s1 == s1 true ");
        System.out.println(s1 == s2);
        System.out.println("s1 == s3 false ");
        System.out.println(s1 == s3);
        System.out.println("x == y false ");
        System.out.println(x == y);
        System.out.println("x.equals(y) false ");
        System.out.println(x.equals(y));
    }
}
