package pro.verron.aoc.utils.assertions;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Assertions {
    private static final AtomicInteger counter = new AtomicInteger();
    public static final String TEMPLATE_NUMBER = "actual was %d, instead of expected %d";
    public static final String TEMPLATE_STR = "actual was %s, instead of expected %s";
    public static void assertEquals(int actual, int expected, String msg) {
        if (actual != expected)
            throw new AssertionError(msg + " " + TEMPLATE_NUMBER.formatted(actual, expected));
    }
    public static void assertEquals(int actual, int expected){
        assertEquals(actual, expected, "assertion n.%d".formatted(counter.getAndIncrement()));
    }
    public static <T> void assertEquals(T actual, T expected, String msg) {
        if (!Objects.equals(actual, expected))
            throw new AssertionError(msg + " " + TEMPLATE_STR.formatted(actual, expected));
    }
    public static <T> void assertEquals(T actual, T expected){
        assertEquals(actual, expected, "assertion n.%s".formatted(counter.getAndIncrement()));
    }
    public static void assertEquals(long actual, long expected, String msg) {
        if (actual != expected)
            throw new AssertionError(msg + " " + TEMPLATE_NUMBER.formatted(actual, expected));
    }
    public static void assertEquals(long actual, long expected){
        assertEquals(actual, expected, "assertion n.%d".formatted(counter.getAndIncrement()));
    }
}
