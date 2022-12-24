package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;
import pro.verron.aoc.utils.board.Coordinate;
import pro.verron.aoc.utils.board.Direction;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.LongStream.rangeClosed;
import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;
import static pro.verron.aoc.utils.board.Direction.DOWN;
import static pro.verron.aoc.utils.board.Direction.UP;

public class Day17 {
    public static void main(String[] args) throws IOException {
        AdventOfCode adventOfCode = new AdventOfCode(22, 17);
        assertEquals(simulateRockFalls(parseDirections(adventOfCode.testString()), 2022L), 3068, "Sample Part 1");
        assertEquals(simulateRockFalls(parseDirections(adventOfCode.trueString()), 2022L), 3114, "Exercice Part 1");
        assertEquals(simulateRockFalls(parseDirections(adventOfCode.testString()), 1000000000000L), 1514285714288L, "Sample Part 2");
        assertEquals(simulateRockFalls(parseDirections(adventOfCode.trueString()), 1000000000000L), 1540804597682L, "Exercice Part 2");
    }
    static final List<Shape> SHAPES = List.of(
            new Shape(List.of("####"), 1, 4, new Coordinate(3, 2)),
            new Shape(List.of(" # ", "###", " # "), 3, 3, new Coordinate(3, 2)),
            new Shape(List.of("###", "  #", "  #"), 3, 3, new Coordinate(3, 2)),
            new Shape(List.of("#", "#", "#", "#"), 4, 1, new Coordinate(3, 2)),
            new Shape(List.of("##", "##"), 2, 2, new Coordinate(3, 2)));
    static LinkedList<Direction> parseDirections(String input) {
        return stream(input.split(""))
                .map(character -> character.equals("<") ? Direction.LEFT : Direction.RIGHT)
                .collect(toCollection(LinkedList::new));
    }
    static long simulateRockFalls(Queue<Direction> jetDirections, long requestedFallen) {
        var cave = new Cave(7, emptyList());
        var directions = new LinkedList<>(jetDirections);
        var shapes = new LinkedList<>(SHAPES);
        Map<Integer, Map<Integer, Map<Cave, SortedMap<Integer, Long>>>> steps = new HashMap<>();
        Shape shape = shapes.remove();
        long nbShapesFallen = 0;
        long skipped = 0;
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
                int stackHeight = cave.stackHeight();
                shape = shapes.remove().move(UP.times(stackHeight), cave);
                int size = shapes.size();
                SortedMap<Integer, Long> heights = steps
                        .computeIfAbsent(size, shapeIndex -> new HashMap<>())
                        .computeIfAbsent(directions.size(), directionIndex -> new HashMap<>())
                        .computeIfAbsent(cave.last(20), subCave -> new TreeMap<>());
                heights.put(stackHeight, nbShapesFallen);
                if(heights.size() >= 3) {
                    int ultimate = heights.lastKey();
                    int penultimate = heights.headMap(ultimate).lastKey();
                    int antepenultimate = heights.headMap(penultimate).lastKey();

                    int deltaHeight = ultimate - penultimate;
                    int penultimateDelta = penultimate - antepenultimate;
                    if(deltaHeight == penultimateDelta) {
                        long deltaFallen = heights.get(ultimate) - heights.get(penultimate);
                        long times = (requestedFallen - nbShapesFallen) / deltaFallen;
                        skipped += deltaHeight * times;
                        nbShapesFallen += deltaFallen * times;
                    }
                }
                nbShapesFallen++;
            }
        } while (nbShapesFallen < requestedFallen);
        return cave.stackHeight() + skipped;
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

    record Cave(int width, List<String> rows) {
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
            return new Cave(width, futureRows);
        }

        public boolean collides(Shape shape) {
            return rangeClosed(shape.bottom(), shape.top()).boxed()
                    .flatMap(i -> rangeClosed(shape.left(), shape.right()).mapToObj(j -> new Coordinate(i, j)))
                    .anyMatch(c -> shape.exists(c) && this.exists(c));
        }
        public int stackHeight() {
            int endIndex = rows.size();
            int fromIndex = max(0, endIndex - 5);
            return (int) rows.subList(fromIndex, endIndex).stream().filter(s -> s.contains("#")).count() + fromIndex;
        }
        public boolean exists(Coordinate target) {
            if (target.row() < 0) return true;
            if (target.row() >= stackHeight()) return false;
            if (target.column() < 0) return true;
            if (target.column() >= width) return true;
            return rows.get((int)(target.row())).charAt((int) target.column()) == '#';
        }

        public Cave last(int nbLines) {
            return new Cave(width, List.copyOf(rows.subList(max(0, rows.size() - nbLines), rows.size())));
        }
    }
}