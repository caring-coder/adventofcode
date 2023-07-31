package pro.verron.aoc.y15;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Day05 {
    public String ex1(List<String> content) {
        Predicate<String> threeVowels = Pattern.compile("(?:[aeiou][^aeiou]*){3}").asMatchPredicate();
        Predicate<String> duplicateCharacter = (str) -> Pattern.compile("(.)\\1").matcher(str).find();
        Predicate<String> naughtySubstrings = Pattern.compile("(ab|cd|pq|xy)").asMatchPredicate();
        return String.valueOf(content.stream()
                .filter(threeVowels)
                .filter(duplicateCharacter)
                .filter(naughtySubstrings)
                .count());
    }

    public String ex2(List<String> content) {
        return "null";
    }
}
