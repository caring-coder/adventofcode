package pro.verron.aoc.core.test;

import org.jetbrains.annotations.NotNull;
import pro.verron.aoc.core.DayInstance;
import pro.verron.aoc.core.Runner;
import pro.verron.aoc.utils.functional.ThrowingFunction;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.util.function.Function;

import static pro.verron.aoc.utils.functional.ThrowingFunction.sneaky;

public record StandardTestRunner(
        Method entrypoint,
        ThrowingFunction<Path, Object> injector,
        DayInstance instance
)
        implements TestRunner {
    public @NotNull Function<Test, TestResult> run1(int exerciseIndex) {
        return sneaky(sample -> test1(sample, exerciseIndex));
    }

    public @NotNull Function<Test, TestResult> run2(int exerciseIndex) {
        return sneaky(sample -> test2(sample, exerciseIndex));
    }

    private TestResult test1(Test test, int idxExercise) throws Exception {
        var in = injector().apply(test.in());
        var out = Runner.readString(test.out());
        var expected = out.split("-----")[idxExercise].trim();
        var start = LocalTime.now();
        var actual = Runner.invoke(instance.value, entrypoint, in);
        var end = LocalTime.now();
        var success = expected.equals(actual);
        return new TestResult(test, success, actual, expected,
                              Duration.between(start, end));
    }

    private TestResult test2(Test test, int idxExercise) throws Exception {
        var in = injector().apply(test.in());
        var out = Runner.readString(test.out());
        var expected = out.split("-----")[idxExercise].trim();
        var start = LocalTime.now();
        var actual = Runner.invoke(instance.value, entrypoint, in);
        var end = LocalTime.now();
        var success = expected.equals(actual);
        return new TestResult(test, success, actual, expected,
                              Duration.between(start, end));
    }
}
