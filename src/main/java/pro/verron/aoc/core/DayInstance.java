package pro.verron.aoc.core;

import org.jetbrains.annotations.NotNull;
import pro.verron.aoc.AdventOfCodeException;
import pro.verron.aoc.core.test.*;

import java.util.List;

import static java.util.Optional.ofNullable;

public class DayInstance {
    public final Object value;

    public DayInstance(Object value) {
        this.value = value;
    }

    public TestSuite test1(List<Test> inputs) {
        return new TestSuite("ex1", inputs.stream()
                                          .map(generateRunner("ex1").run(0))
                                          .toList());
    }

    @NotNull TestRunner generateRunner(String exerciseName) {
        if (value == null)
            return failingRunner();
        var dayClass = value.getClass();
        var entrypoint = Runner.findExerciseMethod(dayClass, exerciseName);
        var delimiter = ofNullable(entrypoint.getAnnotation(Delimiter.class))
                .map(Delimiter::value)
                .orElse("\n");
        var inputType = entrypoint.getParameterTypes()[0];
        var injector = Injector.find(inputType)
                               .map(candidate -> candidate.withDelimiter(delimiter))
                               .orElseThrow(
                                       () -> new AdventOfCodeException(new NoSuchMethodException(
                                               "Could not find an injector for type %s".formatted(
                                                       inputType))));
        return new StandardTestRunner(entrypoint, injector, this);
    }

    private TestRunner failingRunner() {
        return new FailingTestRunner();
    }

    public TestSuite test2(List<Test> inputs) {
        return new TestSuite("ex2", inputs.stream()
                                          .map(generateRunner("ex2").run(1))
                                          .toList());
    }

}
