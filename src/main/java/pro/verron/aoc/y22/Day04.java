package pro.verron.aoc.y22;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class Day04 {
    public String ex1(Stream<String> content) {
        return String.valueOf(campCleanup(content, Assignment::hasFullOverlap));
    }

    public String ex2(Stream<String> content) {
        return String.valueOf(campCleanup(content, Assignment::hasSomeOverlap));
    }

    record Assignment(Set<Integer> assignment1, Set<Integer> assigment2) {
        Assignment(String[] str) {
            this(toRange(str[0]), toRange(str[1]));
        }
        boolean hasFullOverlap() {
            return assignment1.containsAll(assigment2) || assigment2.containsAll(assignment1);
        }
        boolean hasSomeOverlap() {
            return assignment1.stream().anyMatch(assigment2::contains);
        }
    }

    private static long campCleanup(Stream<String> content, Predicate<Assignment> predicate) {
        return content
                .map(pair -> pair.split(","))
                .map(Assignment::new)
                .filter(predicate)
                .count();
    }

    private static Set<Integer> toRange(String str) {
        String[] split = str.split("-");
        int start = parseInt(split[0]);
        int end = parseInt(split[1]);
        return range(start, end + 1).boxed().collect(toSet());
    }
}