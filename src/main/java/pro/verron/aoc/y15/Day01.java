package pro.verron.aoc.y15;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.stream;

public class Day01 {

    public String ex1(String content) {
        return stream(content.split(""))
                .map(chr -> chr.equals("(") ? 1 : -1)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No elfs found");
    }

    public String ex2(String content) {
        AtomicInteger floor = new AtomicInteger(0);
        return String.valueOf(stream(content.split(""))
                .map(chr -> chr.equals("(") ? 1 : -1)
                .takeWhile(i -> floor.getAndAdd(i) != -1)
                .count()
        );
    }
}
