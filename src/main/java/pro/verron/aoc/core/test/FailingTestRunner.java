package pro.verron.aoc.core.test;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.Function;

public class FailingTestRunner
        implements TestRunner {
    @Override
    public @NotNull Function<Test, TestResult> run(int exerciseIndex) {
        return test -> new TestResult(test, false, "No day instance found", "", Duration.ZERO);
    }
}
