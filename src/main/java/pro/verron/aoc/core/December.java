package pro.verron.aoc.core;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

public class December {
    private final int year;
    private final PrintStream writer;

    public December(int year, PrintStream writer) {
        this.year = year;
        this.writer = writer;
    }

    public void run() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String entryYearTemplate = "y%02d";
        String entryDayTemplate = "Day%02d";
        String entryClassTemplate = "pro.verron.aoc.%s.%s";
        for (int day = 1; day <= 25; day++) {
            String entryDay = entryDayTemplate.formatted(day);
            String entryYear = entryYearTemplate.formatted(year);
            String entryClass = entryClassTemplate.formatted(entryYear, entryDay);
            writer.printf("  %s: ", entryDay);
            AdventOfCode adventOfCode;
            Object dayInstance;
            try {
                var dayClass = loader.loadClass(entryClass);
                var constructor = dayClass.getConstructor();
                adventOfCode = new AdventOfCode(year, day);
                dayInstance = constructor.newInstance();
            } catch (ClassNotFoundException |
                     NoSuchMethodException |
                     InvocationTargetException |
                     IllegalAccessException |
                     InstantiationException |
                     RuntimeException e) {
                writer.printf("%s -> %s", e.getClass(), e.getMessage());
                writer.println();
                writer.flush();
                continue;
            }

            try {
                testExercise(adventOfCode, dayInstance, 1);
            } catch (InvocationTargetException
                     | IllegalAccessException
                     | IOException
                     | RuntimeException e
            ) {
                writer.printf("%s -> %s", e.getClass(), e.getMessage());
                writer.println();
                writer.flush();
                continue;
            }
            writer.print(" | ");
            try {
                testExercise(adventOfCode, dayInstance, 2);
            } catch (InvocationTargetException
                     | IllegalAccessException
                     | IOException
                     | RuntimeException e
            ) {
                writer.printf("%s -> %s", e.getClass(), e.getMessage());
                writer.println();
                writer.flush();
                continue;
            }

            writer.println();
            writer.flush();
        }
        writer.flush();
    }

    private void testExercise(AdventOfCode adventOfCode, Object dayInstance, int noExercise) throws InvocationTargetException, IllegalAccessException, IOException {
        var tests = adventOfCode.test(dayInstance, noExercise);
        long nbSucc = tests.stream().filter(TestResult::success).count();
        long nbTest = tests.stream().count();
        boolean validated = tests.stream().allMatch(TestResult::success);
        writer.printf("%d> %-6s %s ", noExercise, "%d/%d".formatted(nbSucc, nbTest),
                validated ? "☑" : "☐");
        if (!validated) {
            writer.println();
            tests.stream().filter(testResult -> !testResult.success()).forEach(writer::println);
        }
    }
}
