package pro.verron.aoc;

import java.util.concurrent.atomic.AtomicInteger;

public class Assertions {

    private static AtomicInteger counter = new AtomicInteger();
    public static final String TEMPLATE = "actual was %d, instead of expected %d";

    public static void assertEquals(int actual, int expected, String msg) {
        assert actual == expected : msg.formatted(actual, expected);
    }

    public static void assertEquals(int actual, int expected){
        assertEquals(actual, expected, "assertion n.%d".formatted(counter.getAndIncrement()));
    }

    public static void assertEquals(long actual, long expected, String msg) {
        assert actual == expected : msg + " " + TEMPLATE.formatted(actual, expected);
    }

    public static void assertEquals(long actual, long expected){
        assertEquals(actual, expected, "assertion n.%d".formatted(counter.getAndIncrement()));
    }
}
