package pro.verron.aoc.y15;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.sort;

public class Day02 {
    private static String ex(Stream<String> content, Function<Box, Integer> wrappingPaperSurface) {
        return content
                .map(line -> line.split("x"))
                .map(Arrays::stream)
                .map(dimensions -> dimensions.mapToInt(Integer::parseInt))
                .map(IntStream::toArray)
                .map(dimensions -> new Box(dimensions[0], dimensions[1], dimensions[2]))
                .map(wrappingPaperSurface)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No box found");
    }

    private static int wrappingPaperSurface(Box box) {
        return box.surface() + box.smallestSizeArea();
    }

    private static int ribbonLength(Box box) {
        return box.smallestSizePerimeter() + box.volume();
    }

    public String ex1(Stream<String> content) {
        return ex(content, Day02::wrappingPaperSurface);
    }

    public String ex2(Stream<String> content) {
        return ex(content, Day02::ribbonLength);
    }

    private record Box(int length, int depth, int height) {
        Box(int length, int depth, int height) {
            int[] dimensions = {length, depth, height};
            sort(dimensions);
            this.length = dimensions[2];
            this.height = dimensions[1];
            this.depth = dimensions[0];
        }

        public int surface() {
            return 2 * (height * length + height * depth + length * depth);
        }

        public int smallestSizeArea() {
            return depth * height;
        }

        public int smallestSizePerimeter() {
            return 2 * (depth + height);
        }

        public int volume() {
            return depth * height * length;
        }
    }
}