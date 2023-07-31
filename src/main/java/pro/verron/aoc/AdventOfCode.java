package pro.verron.aoc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.nio.file.Files.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Stream.concat;
import static pro.verron.aoc.ThrowingFunction.sneaky;

public record AdventOfCode(Path root) {
    AdventOfCode(int year, int day) {
        this(Path.of("input", "y%02d".formatted(year), "d%02d".formatted(day)));
    }

    private static boolean test(Function<Path, Object> extractInput, Object dayInstance, TestRecord s, Method entrypoint, int idxExercise) throws IOException, IllegalAccessException, InvocationTargetException {
        Object in = extractInput.apply(s.in());
        String out = readString(s.out());
        String actual = (String) entrypoint.invoke(dayInstance, in);
        String expected = out.split("-----")[idxExercise].trim();
        return expected.equals(actual);
    }

    private static Method findExerciseMethod(Class<?> dayClass, String nameExercise) {
        return Arrays.stream(dayClass.getMethods())
                .filter(method -> method.getName().equals(nameExercise))
                .findFirst()
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> List.of(String.class, List.class, Stream.class).contains(method.getParameterTypes()[0]))
                .orElseThrow(() -> {
                    String template = "Could not find an method named %s in %s," +
                            " with only one parameter of type String, List<String> or Stream<String>";
                    String message = template.formatted(nameExercise, dayClass);
                    return new RuntimeException(new NoSuchMethodException(message));
                });
    }

    private static Path findOutPath(Path p) {
        String inFilename = p.getFileName().toString();
        String outFilename = inFilename.replace(".in", ".out");
        return p.resolveSibling(outFilename);
    }

    private Path inputPath(String input, int i) {
        String extensionDetail = i == 0 ? "" : "-%d".formatted(i);
        String filename = ("%s%s.txt").formatted(input, extensionDetail);
        return root.resolve(filename);
    }

    private Stream<String> stream(String input, int i, String delimiter) {
        try {
            return new Scanner(inputPath(input, i)).useDelimiter(delimiter).tokens();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TestResult test(Object dayInstance, int noExercise) throws InvocationTargetException, IllegalAccessException, IOException {
        String nameExercise = "ex" + noExercise;
        int idxExercise = noExercise - 1;

        Class<?> dayClass = dayInstance.getClass();
        var entrypoint = findExerciseMethod(dayClass, nameExercise);

        Class<?> parameterType = entrypoint.getParameterTypes()[0];
        if (parameterType.equals(String.class)) {

            return inputs().map(s -> {
                try {
                    return test(sneaky(Files::readString), dayInstance, s, entrypoint, idxExercise);
                } catch (IllegalAccessException | InvocationTargetException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).reduce(
                    new TestResult(0, 0),
                    (TestResult r, Boolean b) -> new TestResult(
                            r.success() + (b ? 1 : 0),
                            r.total() + 1),
                    (r1, r2) -> new TestResult(
                            r1.success() + r2.success(),
                            r1.total() + r2.total()));
        } else if (parameterType.equals(List.class)) {
            return inputs().map(s -> {
                try {
                    List<String> in = readAllLines(s.in());
                    String out = readString(s.out());
                    String actual = (String) entrypoint.invoke(dayInstance, in);
                    String expected = out.split("-----")[idxExercise].trim();
                    return expected.equals(actual);
                } catch (IllegalAccessException | InvocationTargetException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).reduce(
                    new TestResult(0, 0),
                    (TestResult r, Boolean b) -> new TestResult(
                            r.success() + (b ? 1 : 0),
                            r.total() + 1),
                    (r1, r2) -> new TestResult(
                            r1.success() + r2.success(),
                            r1.total() + r2.total()));
        } else if (parameterType.equals(Stream.class)) {
            return inputs().map(s -> {
                try {
                    Stream<String> in = lines(s.in());
                    String out = readString(s.out());
                    String actual = (String) entrypoint.invoke(dayInstance, in);
                    String expected = out.split("-----")[idxExercise].trim();
                    return expected.equals(actual);
                } catch (IllegalAccessException | InvocationTargetException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).reduce(
                    new TestResult(0, 0),
                    (TestResult r, Boolean b) -> new TestResult(
                            r.success() + (b ? 1 : 0),
                            r.total() + 1),
                    (r1, r2) -> new TestResult(
                            r1.success() + r2.success(),
                            r1.total() + r2.total()));
        } else {
            throw new IllegalStateException("Unexpected value: " + parameterType);
        }
    }

    private Stream<TestRecord> inputs() throws IOException {
        return concat(
                records("sample").stream(),
                records("exercise").stream()
        );
    }

    private List<TestRecord> records(String type) throws IOException {
        try (Stream<Path> paths = Files.list(root)) {
            return paths
                    .filter(p -> p.getFileName().toString().startsWith(type))
                    .filter(p -> p.getFileName().toString().endsWith(".in"))
                    .sorted(comparing(Path::getFileName))
                    .map(inPath -> new TestRecord(inPath, findOutPath(inPath)))
                    .toList();
        }
    }
}
