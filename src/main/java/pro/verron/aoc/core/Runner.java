package pro.verron.aoc.core;

import org.jetbrains.annotations.NotNull;
import pro.verron.aoc.Main;
import pro.verron.aoc.core.test.TestSuites;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;


public record Runner(
        LocalDate date,
        DayInstance dayInstance,
        Path inputRoot
) {

    public Runner(LocalDate date, Path inputRoot) {
        this(date, findDayInstance(date), inputRoot);
    }

    public static String invoke(
            Object dayInstance,
            Method entrypoint,
            Object in
    ) {
        try {
            return (String) entrypoint.invoke(dayInstance, in);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Method findExerciseMethod(Class<?> dayClass, String nameExercise) {
        return stream(dayClass.getMethods())
                .filter(withName(nameExercise))
                .filter(withSupportedParameterTypes())
                .filter(withSupportedReturnType())
                .findFirst()
                .orElseThrow(() -> {
                    var template = "Could not find an method named %s in %s," +
                                   " with only one parameter of type String, List<String> or Stream<String>";
                    var message = template.formatted(nameExercise, dayClass);
                    return new RuntimeException(
                            new NoSuchMethodException(message));
                });
    }

    @NotNull
    private static Predicate<Method> withSupportedReturnType() {
        return method -> method.getReturnType() == String.class;
    }

    @NotNull
    private static Predicate<Method> withSupportedParameterTypes() {
        return method -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            boolean oneParameter = parameterTypes.length == 1;
            if (!oneParameter) return false;
            Class<?> firstParameterType = parameterTypes[0];
            List<Class<?>> supportedTypes = List.of(String.class,
                                                    List.class,
                                                    Stream.class
            );
            return supportedTypes
                    .contains(firstParameterType);
        };
    }

    @NotNull
    private static Predicate<Method> withName(String nameExercise) {
        return method -> method.getName()
                .equals(nameExercise);
    }

    static DayInstance findDayInstance(LocalDate date) {
        var loader = ClassLoader.getSystemClassLoader();
        var entryDay = "Day%02d".formatted(date.getDayOfMonth());
        var entryYear = "y%02d".formatted(date.getYear() % 100);
        var entryClass = "pro.verron.aoc.%s.%s".formatted(entryYear, entryDay);
        try {
            var dayClass = loader.loadClass(entryClass);
            var constructor = dayClass.getConstructor();
            var value = constructor.newInstance();
            return new DayInstance(value);
        } catch (ClassNotFoundException |
                 NoSuchMethodException |
                 InvocationTargetException |
                 IllegalAccessException |
                 InstantiationException |
                 RuntimeException e) {
            return new DayInstance(null);
        }
    }

    public TestSuites run() {
        var yearRoot = inputRoot.resolve(
                "y%02d".formatted(date.getYear() % 100));
        var dayPattern = "d%02d".formatted(date.getDayOfMonth());
        var dayRoot = yearRoot.resolve(dayPattern);
        var samples = Main.records(dayRoot, "sample")
                .stream();
        var exercises = Main.records(dayRoot, "exercise")
                .stream();
        var tests = concat(samples, exercises).toList();
        return new TestSuites(date, List.of(dayInstance.test1(tests),
                                            dayInstance.test2(tests)));
    }


}
