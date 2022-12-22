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
    private Path inputPath(String input) {
        return of(INPUT, "y%02d".formatted(year), "day%02d-%s.txt".formatted(day, input));
    }
    private String inputString(String input) throws IOException {
        return readString(inputPath(input));
    }
    public String inputString() throws IOException {
        return inputString(INPUT);
    }
    public String sampleString() throws IOException {
        return inputString(SAMPLE);
    }
    public Stream<String> testStream() throws IOException {
        return trueStream(SAMPLE);
    }
    private Stream<String> trueStream(String input) throws IOException {
        return Files.lines(inputPath(input));
    }
    public Stream<String> trueStream() throws IOException {
        return trueStream(INPUT);
    }

    public List<String> testList() throws IOException {
        return trueList(SAMPLE);
    }public List<String> trueList() throws IOException {
        return trueList(INPUT);
    }

    private List<String> trueList(String input) throws IOException {
        return Files.readAllLines(inputPath(input));
    }
}
