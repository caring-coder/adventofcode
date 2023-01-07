package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;

import java.io.IOException;
import java.util.stream.IntStream;

import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;

public class Day06 {
    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 6);
        assertEquals(tuningTrouble(aoc.testString(), 4), 7L, "Sample Part 1");
        assertEquals(tuningTrouble(aoc.testString(2), 4), 5L, "Sample Part 1");
        assertEquals(tuningTrouble(aoc.testString(3), 4), 6L, "Sample Part 1");
        assertEquals(tuningTrouble(aoc.testString(4), 4), 10L, "Sample Part 1");
        assertEquals(tuningTrouble(aoc.testString(5), 4), 11L, "Sample Part 1");
        assertEquals(tuningTrouble(aoc.trueString(), 4), 1757L, "Exercice Part 1");

        assertEquals(tuningTrouble(aoc.testString(), 14), 19L, "Sample Part 2");
        assertEquals(tuningTrouble(aoc.testString(2), 14), 23L, "Sample Part 2");
        assertEquals(tuningTrouble(aoc.testString(3), 14), 23L, "Sample Part 2");
        assertEquals(tuningTrouble(aoc.testString(4), 14), 29L, "Sample Part 2");
        assertEquals(tuningTrouble(aoc.testString(5), 14), 26L, "Sample Part 2");
        assertEquals(tuningTrouble(aoc.trueString(), 14), 1757L, "Exercice Part 2");
    }

    private static long tuningTrouble(String content, int nb) {
        return IntStream.range(nb - 1, content.length())
                .mapToObj(i -> content.substring(i - nb + 1, i + 1))
                .map(String::chars)
                .map(IntStream::distinct)
                .map(IntStream::count)
                .takeWhile(nbDistinct -> nbDistinct < nb)
                .count() + nb;
    }
}