package pro.verron.aoc.y22;

import pro.verron.aoc.Coordinate;
import pro.verron.aoc.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.LongStream.rangeClosed;
import static pro.verron.aoc.Assertions.assertEquals;
import static pro.verron.aoc.Direction.DOWN;
import static pro.verron.aoc.Direction.UP;

public class Day17 {
    static final List<Shape> SHAPES = List.of(
            new Shape(List.of("####"), 1, 4, new Coordinate(3, 2)),
            new Shape(List.of(" # ", "###", " # "), 3, 3, new Coordinate(3, 2)),
            new Shape(List.of("###", "  #", "  #"), 3, 3, new Coordinate(3, 2)),
            new Shape(List.of("#", "#", "#", "#"), 4, 1, new Coordinate(3, 2)),
            new Shape(List.of("##", "##"), 2, 2, new Coordinate(3, 2)));
    public static void main(String[] args) throws IOException {
        assertEquals(Day17.simulateRockFalls(parseDirections("day17-sample.txt"), 2022L), 3068, "Sample");
        assertEquals(Day17.simulateRockFalls(parseDirections("day17-input.txt"), 2022L), 3114, "Part 1");
        assertEquals(Day17.simulateRockFalls(parseDirections("day17-input.txt"), 1000000000000L), 3068, "Part 2");
    }
    static LinkedList<Direction> parseDirections(String s) throws IOException {
        var input = Files.readString(Path.of("input", "y22", s));
        return stream(input.split(""))
                .map(character -> character.equals("<") ? Direction.LEFT : Direction.RIGHT)
                .collect(toCollection(LinkedList::new));
    }
    static long simulateRockFalls(Queue<Direction> jetDirections, long requestedFallen) {
        var cave = new Cave(7, emptyList(), new Coordinate(0, 0));
        var directions = new LinkedList<>(jetDirections);
        var shapes = new LinkedList<>(SHAPES);
        Map<Integer, Map<Integer, Map<Cave, SortedMap<Long, Long>>>> steps = new HashMap<>();
        Shape shape = shapes.remove();
        long nbShapesFallen = 0;
        do {
            if (directions.isEmpty()) directions.addAll(jetDirections);
            Direction direction = directions.remove();

            if (shape.canMove(direction, cave)) shape = shape.move(direction, cave);
            if (shape.canMove(DOWN, cave))
                shape = shape.move(DOWN, cave);
            else {
                cave = cave.insert(shape);
                if (shapes.isEmpty())
                    shapes.addAll(SHAPES);
                long stackHeight = cave.stackHeight();
                shape = shapes.remove().move(UP.times(stackHeight), cave);
                int size = shapes.size();
                SortedMap<Long, Long> heights = steps
                        .computeIfAbsent(size, shapeIndex -> new HashMap<>())
                        .computeIfAbsent(directions.size(), directionIndex -> new HashMap<>())
                        .computeIfAbsent(cave.last(20), subCave -> new TreeMap<>());
                heights.put(stackHeight, nbShapesFallen);
                if(heights.size() >= 3) {
                    System.out.println("Made a loop");
                    long ultimate = heights.lastKey();
                    long penultimate = heights.headMap(ultimate).lastKey();
                    long antepenultimate = heights.headMap(penultimate).lastKey();

                    long deltaHeight = ultimate - penultimate;
                    long penultimateDelta = penultimate - antepenultimate;
                    if(deltaHeight == penultimateDelta) {
                        System.out.println("Made a stable loop");
                        long deltaFallen = heights.get(ultimate) - heights.get(penultimate);
                        long times = (requestedFallen - nbShapesFallen) / deltaFallen;
                        cave = cave.skip(deltaHeight * times);
                        nbShapesFallen += deltaFallen * times;
                    }
                }
                nbShapesFallen++;
                System.out.println(nbShapesFallen);
            }
        } while (nbShapesFallen < requestedFallen);
        return cave.stackHeight();
    }
    record Shape(List<String> s, int height, int width, Coordinate coordinate) {
        public long bottom() {
            return coordinate().row();
        }
        public long top() {
            return bottom() + height() - 1;
        }
        public long left() {
            return coordinate.column();
        }
        public long right() {
            return left() + width() - 1;
        }
        public Shape move(Direction direction, Cave cave) {
            var nextHeight = coordinate.row() + direction.height();
            int leftMost = cave.width() - this.width;
            var suggestedCoordinate = coordinate.column() + direction.right();
            var nextRight = max(0, min(suggestedCoordinate, leftMost));
            var nextCoordinate = new Coordinate(nextHeight, nextRight);
            return new Shape(s, height, width, nextCoordinate);
        }
        public boolean canMove(Direction direction, Cave cave) {
            return !cave.collides(this.move(direction, cave));
        }
        public boolean exists(Coordinate target) {
            if (target.row() < bottom()) return false;
            if (target.row() > top()) return false;
            if (target.column() < left()) return false;
            if (target.column() > right()) return false;
            return existsInShapeCoordinate(target.row() - coordinate.row(), target.column() - coordinate.column());
        }
        private boolean existsInShapeCoordinate(long i, long j) {
            return s.get((int)i).charAt((int)j) == '#';
        }
    }

    record Cave(int width, List<String> rows, Coordinate coordinate) {
        public Cave insert(Shape shape) {
            int index = max(0, rows.size() - 20);
            LinkedList<String> futureRows = new LinkedList<>(rows.subList(0, index));
            for (int i = index; i <= max(stackHeight(), shape.top()); i++) {
                StringBuilder sb = new StringBuilder(rows.size() <= i ? " ".repeat(width) : rows.get(i));
                if (shape.bottom() <= i && i <= shape.top()) {
                    for (int j = 0; j < width; j++) {
                        Coordinate target = new Coordinate(i, j);
                        sb.replace(j, j + 1, shape.exists(target) || this.exists(target) ? "#" : " ");
                    }
                }
                futureRows.add(sb.toString());
            }
            return new Cave(width, futureRows, coordinate);
        }

        public boolean collides(Shape shape) {
            return rangeClosed(shape.bottom(), shape.top()).boxed()
                    .flatMap(i -> rangeClosed(shape.left(), shape.right()).mapToObj(j -> new Coordinate(i, j)))
                    .anyMatch(c -> shape.exists(c) && this.exists(c));
        }
        public long stackHeight() {
            int endIndex = rows.size();
            int fromIndex = max(0, endIndex - 5);
            return rows.subList(fromIndex, endIndex).stream().filter(s -> s.contains("#")).count() + coordinate.row() + fromIndex;
        }
        public boolean exists(Coordinate target) {
            if (target.row() < coordinate.row()) return true;
            if (target.row() >= coordinate.row() + stackHeight()) return false;
            if (target.column() < coordinate.column()) return true;
            if (target.column() >= coordinate.column() + width) return true;
            return existsInShapeCoordinate(target.row() - coordinate.row(), target.column() - coordinate.column());
        }
        private boolean existsInShapeCoordinate(long i, long j) {
            return rows.get((int)(i)).charAt((int)j) == '#';
        }

        public Cave skip(long height) {
            return new Cave(width, rows, new Coordinate(coordinate.row() + height, coordinate.column()));
        }

        public Cave last(int nbLines) {
            return new Cave(width, List.copyOf(rows.subList(max(0, rows.size() - nbLines), rows.size())), coordinate);
        }
    }
}