package pro.verron.aoc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import pro.verron.aoc.core.Runner;
import pro.verron.aoc.core.test.Test;
import pro.verron.aoc.core.test.TestSuite;
import pro.verron.aoc.core.test.TestSuites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.time.Month.DECEMBER;
import static java.util.Comparator.comparing;
import static java.util.stream.IntStream.iterate;
import static java.util.stream.IntStream.rangeClosed;

/**
 * This class is the entry point of the advent of code runner for the
 * challenges hosted on <a href="https://adventofcode.com">adventofcode.com</a>
 * <p>
 * It will run all the challenges for all the years from 2015 to 2022
 * that are present in the input folder.
 * <p>
 * The input folder should be structured as follow:
 * <pre>
 *     input
 *     ├── y15
 *     │   ├── d01
 *     │   │   ├── ex1.in
 *     │   │   └── ex2.in
 *     │   ├── d02
 *     │   │   ├── ex1.in
 *     │   │   └── ex2.in
 *     │   └── ...
 *     ├── y16
 *     │   ├── d01
 *     │   │   ├── ex1.in
 *     │   │   └── ex2.in
 *     │   ├── d02
 *     │   │   ├── ex1.in
 *     │   │   └── ex2.in
 *     │   └── ...
 *     ├── y17
 *     │   ├── d01
 *     │   │   ├── ex1.in
 *     │   │   └── ex2.in
 *     │   ├── d02
 *     │   │   ├── ex1.in
 *     │   │   └── ex2.in
 *     │   └── ...
 *     ├── ...
 *     └── y22
 * </pre>
 */
public class Main {

    public static final Path INPUT_ROOT = Path.of("input");
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        iterate(2015, year -> year + 1)
                .mapToObj(year -> YearMonth.of(year, DECEMBER))
                .flatMap(Main::first25days)
                .takeWhile(LocalDate.now()::isAfter)
                .map(date -> new Runner(date, INPUT_ROOT).run())
                .forEachOrdered(Main::logsResults);
    }

    @NotNull
    private static Stream<LocalDate> first25days(YearMonth month) {
        return rangeClosed(1, 25).mapToObj(month::atDay);
    }

    public static List<Test> records(Path root, String type) {
        try (var files = Files.list(root)) {
            return files.filter(p -> prefixedBy(p, type))
                    .filter(p -> suffixedWith(p, ".in"))
                    .sorted(comparing(Path::getFileName))
                    .map(Test::new)
                    .toList();
        } catch (NoSuchFileException e) {
            return List.of();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean suffixedWith(Path p, String suffix) {
        return p.getFileName()
                .toString()
                .endsWith(suffix);
    }

    private static boolean prefixedBy(Path path, String prefix) {
        return path.getFileName()
                .toString()
                .startsWith(prefix);
    }

    public static void logsResults(TestSuites suites) {
        var prefix = "%04d dec %02d : ".formatted(suites.date()
                                                          .getYear(),
                                                  suites.date()
                                                          .getDayOfMonth());
        var statusTemplate = "%-4s %s %3d/%-3d %3ds%03dms ";
        var errorTemplate = "expected %10s but was %-10s >>> %s";
        var statuses = new StringBuilder(prefix);
        var errors = new ArrayList<String>();
        for (TestSuite suite : suites.testSuites()) {
            statuses.append(suite.statusMessage(statusTemplate));
            errors.addAll(suite.errorMessages(errorTemplate));
        }
        Level level =
                errors.isEmpty() && suites.duration()
                                            .compareTo(Duration.of(
                                                    (long) suites.testSuites()
                                                            .size() * 2L
                                                    , ChronoUnit.SECONDS)) < 0 ?
                        Level.INFO :
                        Level.ERROR;
        log.atLevel(level)
                .log(statuses.toString());
        errors.forEach(log::error);
    }
}
