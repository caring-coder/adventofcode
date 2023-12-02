package pro.verron.aoc.y15;

import pro.verron.aoc.utils.Box;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day02 {
    private static String ex(Stream<String> content, Box.BoxMeasurer measurer) {
        return content
                .map(line -> line.split("x"))
                .map(Arrays::stream)
                .map(dimensions -> dimensions.mapToInt(Integer::parseInt))
                .map(IntStream::toArray)
                .map(Box::new)
                .map(measurer::measure)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No box found");
    }

    public String ex1(Stream<String> content) {
        return ex(content, Box::wrappingPaperSurface);
    }

    public String ex2(Stream<String> content) {
        return ex(content, Box::ribbonLength);
    }
}
