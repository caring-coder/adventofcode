package pro.verron.aoc.utils;

import static java.util.Arrays.sort;

public record Box(int length, int depth, int height) {

    public Box(int[] dimensions) {
        this(dimensions[0], dimensions[1], dimensions[2]);
    }

    public Box(int length, int depth, int height) {
        int[] dimensions = {length, depth, height};
        sort(dimensions);
        this.length = dimensions[2];
        this.height = dimensions[1];
        this.depth = dimensions[0];
    }

    public int wrappingPaperSurface() {
        return surface() + smallestSizeArea();
    }

    public int ribbonLength() {
        return smallestSizePerimeter() + volume();
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

    public interface BoxMeasurer {
        int measure(Box box);
    }
}
