package pro.verron.aoc.y22;

import java.util.stream.IntStream;

public class Day06 {
    public String ex1(String content) {
        return String.valueOf(tuningTrouble(content, 4));
    }

    public String ex2(String content) {
        return String.valueOf(tuningTrouble(content, 14));
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