package pro.verron.aoc.y23;

import java.util.stream.Stream;

public class Day01 {


    public String ex1(Stream<String> content) {
        return content
                .map(Day01::parseDigits)
                .mapToInt(Day01::addFirstAndLast)
                .sum();
    }

    public String ex2(Stream<String> content) {

    }


}
