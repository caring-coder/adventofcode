package pro.verron.aoc.y15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.Arrays.*;
import static java.util.Arrays.stream;

public class Day2 {
    public static void main(String[] args) throws IOException {
        Path source = Path.of("input","y15", "day02-input.txt");
        out.println(ex1(Files.lines(source)));
        out.println(ex2(Files.lines(source)));
    }

    private static String ex1(Stream<String> content) {
        return content
                .map(line -> line.split("x"))
                .map(line -> stream(line).mapToInt(Integer::parseInt).toArray())
                .map(dimensions -> new Box(dimensions[0], dimensions[1], dimensions[2]))
                .map(box -> box.surface() + box.smallestSizeArea())
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No elfs found");
    }

    private static String ex2(Stream<String> content) {
        return content
                .map(line -> line.split("x"))
                .map(line -> stream(line).mapToInt(Integer::parseInt).toArray())
                .map(dimensions -> new Box(dimensions[0], dimensions[1], dimensions[2]))
                .map(box -> box.smallestSizePerimeter() + box.volume())
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No elfs found");
    }


    private record Box(int length, int depth, int height) {
        Box(int length, int depth, int height){
            int[] dimensions = {length, depth, height};
            sort(dimensions);
            this.length = dimensions[2];
            this.height = dimensions[1];
            this.depth = dimensions[0];
        }
        public int surface() {
                return 2 * height * length
                        + 2 * height * depth
                        + 2 * length * depth;
        }

        public int smallestSizeArea() {
            return depth * height;
        }

        public int smallestSizePerimeter() {
            return 2 * depth
                    + 2 * height;
        }

        public int volume() {
            return depth * height * length;
        }
    }
}
