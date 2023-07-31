package pro.verron.aoc;

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
            } catch (ClassNotFoundException
                     | NoSuchMethodException
                     | InvocationTargetException
                     | IllegalAccessException
                     | InstantiationException
                     | RuntimeException e
            ) {
                writer.printf("%s -> %s", e.getClass(), e.getMessage());
                writer.println();
                writer.flush();
                continue;
            }

            try {
                var test = adventOfCode.test(dayInstance, 1);
                writer.printf("1> %-6s %s ",
                        "%d/%d".formatted(test.success(), test.total()),
                        test.validate() ? "☑" : "☐");
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
                var test = adventOfCode.test(dayInstance, 2);
                writer.printf("2> %-6s %s ",
                        "%d/%d".formatted(test.success(), test.total()),
                        test.validate() ? "☑" : "☐");
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
}
