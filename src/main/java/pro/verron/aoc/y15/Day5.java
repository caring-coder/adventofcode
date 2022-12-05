package pro.verron.aoc.y15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.out;

public class Day5 {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Path source = Path.of("input","y15", "day5-input.txt");
        out.println(ex1(Files.readAllLines(source)));
    }

    private static long ex1(List<String> content) {
        Predicate<String> threeVowels = Pattern.compile("(?:[aeiou][^aeiou]*){3}").asMatchPredicate();
        Predicate<String> duplicateCharacter = (str) -> Pattern.compile("(.)\\1").matcher(str).find();
        Predicate<String> naughtySubstrings = Pattern.compile("(ab|cd|pq|xy)").asMatchPredicate();
        return content.stream()
                .filter(threeVowels)
                .filter(duplicateCharacter)
                .filter(naughtySubstrings)
                .count();
    }
}
