package pro.verron.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.readString;
import static java.nio.file.Path.of;

public record AdventOfCode(int year, int day) {
    public static final String INPUT = "input";
    public static final String SAMPLE = "sample";
    private Path inputPath(String input, int i) {
        return of(INPUT, "y%02d".formatted(year), "day%02d-%s%s.txt".formatted(day, input, i==0? "" : "-%d".formatted(i)));
    }
    private String trueString(String input, int i) throws IOException {
        return readString(inputPath(input, i));
    }
    public String trueString() throws IOException {
        return trueString(INPUT, 0);
    }
    public String testString() throws IOException {
        return trueString(SAMPLE, 0);
    }
    public Stream<String> testStream() throws IOException {
        return trueStream(SAMPLE);
    }
    private Stream<String> trueStream(String input) throws IOException {
        return Files.lines(inputPath(input, 0));
    }
    public Stream<String> trueStream() throws IOException {
        return trueStream(INPUT);
    }

    public List<String> testList() throws IOException {
        return trueList(SAMPLE, 0);
    }public List<String> trueList() throws IOException {
        return trueList(INPUT, 0);
    }

    private List<String> trueList(String input, int i) throws IOException {
        return Files.readAllLines(inputPath(input, i));
    }

    public String testString(int i) throws IOException {
        return trueString(SAMPLE, i);
    }

    public List<String> testList(int i) throws IOException {
        return trueList(SAMPLE, i);
    }
}
