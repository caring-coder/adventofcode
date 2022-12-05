package pro.verron.aoc.y15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

public class Day1 {
    public static void main(String[] args) throws IOException {
        Path source = Path.of("input","y15", "day1-input.txt");
        String content = Files.readString(source);
        out.println(ex1(content));
        out.println(ex2(content));
    }

    private static String ex1(String content) {
        return stream(content.split(""))
                .map(chr -> chr.equals("(") ? 1 : -1)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No elfs found");
    }

    private static String ex2(String content) {
        AtomicInteger floor = new AtomicInteger(0);
        return String.valueOf(stream(content.split(""))
                .map(chr -> chr.equals("(") ? 1 : -1)
                .takeWhile(i -> floor.getAndAdd(i) != -1)
                .count()
        );
    }
}
