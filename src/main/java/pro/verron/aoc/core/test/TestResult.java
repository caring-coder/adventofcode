package pro.verron.aoc.core.test;

import java.time.Duration;

public record TestResult(
        Test test,
        boolean success,
        String actual,
        String expected,
        Duration duration
) {
    String dataset() {
        return test.dataset();
    }

    public boolean failure() {
        return !success;
    }
}
