package pro.verron.aoc.y15;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Arrays.binarySearch;
import static java.util.regex.Pattern.compile;
import static java.util.stream.IntStream.range;

public class Day05 {

    private static Predicate<String> containsNCharsFrom(int n, String letters) {
        return str -> containsNCharsFrom(n, letters, str);
    }

    private static boolean containsNCharsFrom(
            int n,
            String letters,
            String str
    ) {
        var chars = letters.chars()
                .sorted()
                .toArray();
        return str.chars()
                .filter(letter -> binarySearch(chars, letter) > -1)
                .skip(n - 1)
                .findAny()
                .isPresent();
    }

    public String ex1(List<String> content) {
        Predicate<String> duplicateCharacter = (str) -> compile(
                "(.)\\1").matcher(str)
                .find();
        Predicate<String> naughtySubstrings = compile(
                "(ab)|(cd)|(pq)|(xy)")
                .asPredicate()
                .negate();
        return String.valueOf(content.stream()
                                      .filter(containsNCharsFrom(3, "aeiou"))
                                      .filter(duplicateCharacter)
                                      .filter(naughtySubstrings)
                                      .count());
    }

    public String ex2(List<String> content) {
        Predicate<String> twoNonOverlappingPairs = this::twoNonOverlappingPairs;
        Predicate<String> oneRepeatingWithGap = (str) -> compile(
                "(.).\\1")
                .matcher(str)
                .find();
        return String.valueOf(content.stream()
                                      .filter(twoNonOverlappingPairs)
                                      .filter(oneRepeatingWithGap)
                                      .count());
    }


    private boolean twoNonOverlappingPairs(String s) {
        record Pair<T>(T x, T y) {}
        return range(0, s.length() - 1).boxed()
                .flatMap(i1 -> range(i1 + 2, s.length() - 1).mapToObj(
                        i2 -> new Pair<>(i1, i2)))
                .map(p -> new Pair<>(s.substring(p.x, p.x + 2),
                                     s.substring(p.y, p.y + 2)))
                .anyMatch(p -> Objects.equals(p.x, p.y));

    }
}
