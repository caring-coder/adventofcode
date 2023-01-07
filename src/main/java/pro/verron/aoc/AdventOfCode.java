package pro.verron.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.nio.file.Path.of;

public record AdventOfCode(int year, int day) {
    public static final String INPUT = "input";
    public static final String SAMPLE = "sample";
    private Path inputPath(String input, int i) {
        String yearFolder = "y%02d".formatted(year);
        String extensionDetail = i == 0 ? "" : "-%d".formatted(i);
        String dayFile = "day%02d-%s%s.txt".formatted(day, input, extensionDetail);
        return of(INPUT, yearFolder, dayFile);
    }
    private String string(String input, int i) throws IOException {
        return readString(inputPath(input, i));
    }
    private Stream<String> stream(String input, int i) throws IOException {
        return lines(inputPath(input, i));
    }

    private Stream<String> stream(String input, int i, String delimiter) throws IOException {
        return new Scanner(inputPath(input, i)).useDelimiter(delimiter).tokens();
    }

    private List<String> list(String input, int i) throws IOException {
        return Files.readAllLines(inputPath(input, i));
    }
    public String trueString() throws IOException {
        return string(INPUT, 0);
    }
    public String testString() throws IOException {
        return string(SAMPLE, 0);
    }
    public Stream<String> testStream() throws IOException {
        return stream(SAMPLE, 0);
    }
    public Stream<String> trueStream() throws IOException {
        return stream(INPUT, 0);
    }

    public Stream<String> trueStream(String delimiter) throws IOException {
        return stream(INPUT, 0, delimiter);
    }

    public Stream<String> trueStream(int i, String delimiter) throws IOException {
        return stream(INPUT, i, delimiter);
    }

    public List<String> testList() throws IOException {
        return list(SAMPLE, 0);
    }
    public List<String> trueList() throws IOException {
        return list(INPUT, 0);
    }
    public String testString(int i) throws IOException {
        return string(SAMPLE, i);
    }
    public Stream<String> testStream(int i) throws IOException {
        return stream(SAMPLE, i);
    }

    public Stream<String> testStream(String delimiter) throws IOException {
        return stream(SAMPLE, 0, delimiter);
    }

    public Stream<String> testStream(int i, String delimiter) throws IOException {
        return stream(SAMPLE, i, delimiter);
    }
}
