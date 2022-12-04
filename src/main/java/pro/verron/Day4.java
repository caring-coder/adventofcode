package pro.verron;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class Day4 {

    public static void main(String[] args) throws IOException {
        Path source = Path.of("day4-input.txt");
        out.println(day4(Files.lines(source)));
        out.println(day4bis(Files.lines(source)));
    }

    private static String day4(Stream<String> content) {
        return String.valueOf(content
                .map(pair -> pair.split(","))
                .map(Arrays::stream)
                .map(pair -> pair.map(Day4::toRange))
                .map(Stream::toList)
                .filter(l -> l.get(0).containsAll(l.get(1)) || l.get(1).containsAll(l.get(0)))
                .count());
    }

    private static String day4bis(Stream<String> content) {
        long count = content
                .map(pair -> pair.split(","))
                .map(Arrays::stream)
                .map(pair -> pair.map(Day4::toRange))
                .map(pair -> pair.reduce(Day4::intersect).orElseThrow())
                .filter(l -> !l.isEmpty())
                .count();
        return String.valueOf(count);
    }

    private static <T> Set<T> intersect(Set<T> accumulator, Set<T> current) {
        return accumulator.stream().filter(current::contains).collect(toSet());
    }

    private static Set<Integer> toRange(String str) {
        String[] split = str.split("-");
        int start = parseInt(split[0]);
        int end = parseInt(split[1]);
        return range(start, end + 1).boxed().collect(toSet());
    }
}
