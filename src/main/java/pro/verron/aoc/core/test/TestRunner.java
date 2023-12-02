package pro.verron.aoc.core.test;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TestRunner {
    @NotNull Function<Test, TestResult> run1(int exerciseIndex);

    @NotNull Function<Test, TestResult> run2(int exerciseIndex);
}
