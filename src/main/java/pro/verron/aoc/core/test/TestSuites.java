package pro.verron.aoc.core.test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class TestSuites {
    private final LocalDate date;
    private final List<TestSuite> testSuites;

    public TestSuites(LocalDate date, List<TestSuite> testSuites) {
        this.date = date;
        this.testSuites = testSuites;
    }

    public List<TestSuite> testSuites() {
        return testSuites;
    }

    public LocalDate date() {
        return date;
    }

    public Duration duration() {
        return testSuites.stream()
                .map(TestSuite::duration)
                .reduce(Duration::plus)
                .orElse(Duration.ZERO);
    }
}
