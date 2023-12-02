package pro.verron.aoc.core.test;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

public record TestSuite(
        String name,
        List<TestResult> results
) {
    public String statusMessage(String template) {
        Duration duration = results.stream()
                .map(TestResult::duration)
                .reduce(Duration::plus)
                .orElse(Duration.ZERO);
        return template.formatted(
                name(),
                success() ? "☑" : "☐",
                nbSuccess(),
                nbTests(),
                duration.getSeconds(),
                duration.getNano() / 1_000_000);
    }

    @NotNull
    public List<String> errorMessages(String errorTemplate) {
        String template = "%s: %s".formatted(name, errorTemplate);
        return errors()
                .stream()
                .map(obj -> template.formatted(
                        obj.expected(),
                        obj.actual(),
                        obj.dataset()
                ))
                .toList();
    }

    int nbTests() {
        return results()
                .size();
    }

    long nbSuccess() {
        return results()
                .stream()
                .filter(TestResult::success)
                .count();
    }

    boolean success() {
        boolean hasTests = !results().isEmpty();
        boolean hasNoFailures = results().stream()
                .noneMatch(TestResult::failure);
        return hasTests && hasNoFailures;
    }

    public List<TestResult> errors() {
        return results().stream()
                .filter(TestResult::failure)
                .toList();
    }

    public Duration duration() {
        return results().stream()
                .map(TestResult::duration)
                .reduce(Duration::plus)
                .orElse(Duration.ZERO);
    }
}
