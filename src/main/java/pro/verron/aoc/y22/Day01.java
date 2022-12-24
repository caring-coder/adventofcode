package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;

public class Day01 {
    public static void main(String[] args) throws IOException {
        AdventOfCode adventOfCode = new AdventOfCode(22, 1);
        assertEquals(calorieCounting(adventOfCode.testString(), 1), 24000, "Sample Part 1");
        assertEquals(calorieCounting(adventOfCode.trueString(), 1), 70369, "Exercice Part 1");
        assertEquals(calorieCounting(adventOfCode.testString(), 3), 45000, "Sample Part 2");
        assertEquals(calorieCounting(adventOfCode.trueString(), 3), 203002, "Exercice Part 2");
    }
    private static int calorieCounting(String content, int nbElfs) {
        return stream(content.split("\r?\n\r?\n"))
                .map(String::lines)
                .map(bag -> bag.mapToInt(Integer::parseInt))
                .map(IntStream::sum)
                .sorted(Comparator.reverseOrder())
                .limit(nbElfs)
                .reduce(0, Integer::sum);
    }
}
