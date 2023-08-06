package pro.verron.aoc.core;

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
        Object in = extractInput.apply(test.in());
        String out = readString(test.out());
        String actual = invoke(dayInstance, entrypoint, in);
        String expected = out.split("-----")[idxExercise].trim();
        boolean success = expected.equals(actual);
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

    public List<TestResult> test(Object dayInstance, int noExercise) throws InvocationTargetException, IllegalAccessException, IOException {
        String nameExercise = "ex" + noExercise;
        int idxExercise = noExercise - 1;

        Class<?> dayClass = dayInstance.getClass();
        var entrypoint = findExerciseMethod(dayClass, nameExercise);

        Class<?> parameterType = entrypoint.getParameterTypes()[0];
        if (parameterType.equals(String.class))
            return getTestResult(dayInstance, idxExercise, entrypoint, sneaky(Files::readString));
        else if (parameterType.equals(List.class))
            return getTestResult(dayInstance, idxExercise, entrypoint, sneaky(Files::readAllLines));
        else if (parameterType.equals(Stream.class))
            return getTestResult(dayInstance, idxExercise, entrypoint, sneaky(Files::lines));
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