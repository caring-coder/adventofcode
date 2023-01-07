package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;

import java.io.IOException;
import java.util.stream.Stream;

import static java.util.Comparator.reverseOrder;
import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;

public class Day01 {
    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 1);
        assertEquals(new Day01().calorieCounting(aoc.testStream("\n\n"), 1), 24000, "Sample Part 1");
        assertEquals(new Day01().calorieCounting(aoc.trueStream("\n\n"), 1), 70369, "Exercice Part 1");
        assertEquals(new Day01().calorieCounting(aoc.testStream("\n\n"), 3), 45000, "Sample Part 2");
        assertEquals(new Day01().calorieCounting(aoc.trueStream("\n\n"), 3), 203002, "Exercice Part 2");
    }
    private int calorieCounting(Stream<String> content, int nbElfs) {
        return content
                .map(s -> s.lines().mapToInt(Integer::parseInt).sum())
                .sorted(reverseOrder())
                .limit(nbElfs)
                .reduce(Integer::sum)
                .orElseThrow();
    }
}
