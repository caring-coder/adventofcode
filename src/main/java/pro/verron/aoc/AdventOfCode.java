package pro.verron.aoc;

import java.io.IOException;
import java.nio.file.Path;

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
}
