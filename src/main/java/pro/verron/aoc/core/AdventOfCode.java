package pro.verron.aoc.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Stream.concat;
import static pro.verron.aoc.utils.board.ThrowingFunction.sneaky;

public record AdventOfCode(Path root) {
    AdventOfCode(int year, int day) {
        this(Path.of("input", "y%02d".formatted(year), "d%02d".formatted(day)));
    }

    private static TestResult test(
            Function<Path, Object> extractInput,
            Object dayInstance,
            Test test,
            Method entrypoint,
            int idxExercise
    ) {
        var in = extractInput.apply(test.in());
        var out = readString(test.out());
        var actual = invoke(dayInstance, entrypoint, in);
        var expected = out.split("-----")[idxExercise].trim();
        var success = expected.equals(actual);
        return new TestResult(test, success, actual, expected);
    }

    private static String invoke(Object dayInstance, Method entrypoint, Object in) {
        try {
            return (String) entrypoint.invoke(dayInstance, in);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Method findExerciseMethod(Class<?> dayClass, String nameExercise) {
        var supportedInjections = List.of(String.class, List.class, Stream.class);
        return stream(dayClass.getMethods())
                .filter(method -> method.getName().equals(nameExercise))
                .findFirst()
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> supportedInjections.contains(method.getParameterTypes()[0]))
                .orElseThrow(() -> {
                    var template = "Could not find an method named %s in %s," +
                            " with only one parameter of type String, List<String> or Stream<String>";
                    var message = template.formatted(nameExercise, dayClass);
                    return new RuntimeException(new NoSuchMethodException(message));
                });
    }

    private static Path findOutPath(Path p) {
        var inFilename = p.getFileName().toString();
        var outFilename = inFilename.replace(".in", ".out");
        return p.resolveSibling(outFilename);
    }

    private static Stream<String> load(Path path, String delimiter) throws IOException {
        return new Scanner(path, StandardCharsets.UTF_8)
                .useDelimiter(delimiter)
                .tokens()
                .peek(System.out::println);
    }

    public List<TestResult> test(Object dayInstance, int noExercise) throws InvocationTargetException, IllegalAccessException, IOException {
        var nameExercise = "ex" + noExercise;
        var idxExercise = noExercise - 1;

        var dayClass = dayInstance.getClass();
        var entrypoint = findExerciseMethod(dayClass, nameExercise);
        var delimiter = Optional
                .ofNullable(entrypoint.getAnnotation(AdventOfCodeDelimiter.class))
                .map(AdventOfCodeDelimiter::value)
                .orElse("\n");

        var parameterType = entrypoint.getParameterTypes()[0];
        if (parameterType.equals(String.class))
            return getTestResult(dayInstance, idxExercise, entrypoint, sneaky(Files::readString));
        else if (parameterType.equals(List.class))
            return getTestResult(dayInstance, idxExercise, entrypoint, sneaky(path -> load(path, delimiter).toList()));
        else if (parameterType.equals(Stream.class))
            return getTestResult(dayInstance, idxExercise, entrypoint, sneaky(path -> load(path, delimiter)));
        else
            throw new IllegalStateException("Unexpected value: " + parameterType);
    }

    private List<TestResult> getTestResult(Object dayInstance, int idxExercise, Method entrypoint, Function<Path, Object> sneaky) throws IOException {
        return concat(records("sample").stream(), records("exercise").stream())
                .map(sample -> test(sneaky, dayInstance, sample, entrypoint, idxExercise))
                .toList();
    }

    private List<Test> records(String type) throws IOException {
        try (Stream<Path> paths = Files.list(root)) {
            return paths
                    .filter(p -> p.getFileName().toString().startsWith(type))
                    .filter(p -> p.getFileName().toString().endsWith(".in"))
                    .sorted(comparing(Path::getFileName))
                    .map(inPath -> new Test(inPath, findOutPath(inPath)))
                    .toList();
        }
    }
}
