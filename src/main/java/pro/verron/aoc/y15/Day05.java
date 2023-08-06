package pro.verron.aoc.y15;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Day05 {
    public String ex1(List<String> content) {
        Predicate<String> threeVowels = str -> str.replaceAll("[^aeiou]", "").length() >= 3;
        Predicate<String> duplicateCharacter = (str) -> Pattern.compile("(.)\\1").matcher(str).find();
        Predicate<String> naughtySubstrings = Pattern.compile("(ab)|(cd)|(pq)|(xy)").asPredicate().negate();
        return String.valueOf(content.stream()
                .filter(threeVowels)
                .filter(duplicateCharacter)
                .filter(naughtySubstrings)
                .count());
    }

    public String ex2(List<String> content) {
        Predicate<String> twoNonOverlappingPairs = this::twoNonOverlappingPairs;
        Predicate<String> oneRepeatingWithGap = (str) -> Pattern.compile("(.).\\1").matcher(str).find();
        return String.valueOf(content.stream()
                .filter(twoNonOverlappingPairs)
                .filter(oneRepeatingWithGap)
                .count());
    }


    private boolean twoNonOverlappingPairs(String s) {
        record Pair(char o, char t) {
        }
        AtomicInteger overlapping = new AtomicInteger();
        Set<Pair> pairs = new HashSet<>();
        Optional<Pair> lastPair = Optional.empty();
        for (int i = 0; i < s.toCharArray().length - 1; i++) {
            Pair newPair = new Pair(s.toCharArray()[i], s.toCharArray()[i + 1]);
            lastPair.filter(newPair::equals)
                    .ifPresent(ignored -> overlapping.getAndIncrement());
            lastPair = Optional.of(newPair);
            pairs.add(newPair);
            if (pairs.size() + overlapping.get() < i)
                return true;
        }
        return false;
    }
}
