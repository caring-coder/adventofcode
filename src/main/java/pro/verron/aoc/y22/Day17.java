package pro.verron.aoc.y22;

import pro.verron.aoc.utils.board.Direction;
import pro.verron.aoc.utils.board.Vector;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.LongStream.rangeClosed;
import static pro.verron.aoc.utils.board.Direction.DOWN;
import static pro.verron.aoc.utils.board.Direction.UP;

public class Day17 {
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
                    int antePenultimate = heights.headMap(penultimate).lastKey();

                    int deltaHeight = ultimate - penultimate;
                    int penultimateDelta = penultimate - antePenultimate;
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

    public String ex1(String content) {
        return String.valueOf(simulateRockFalls(parseDirections(content), 2022L));
    }
    static final List<Shape> SHAPES = List.of(
            new Shape(List.of("####"), 1, 4, new Vector(3, 2)),
            new Shape(List.of(" # ", "###", " # "), 3, 3, new Vector(3, 2)),
            new Shape(List.of("###", "  #", "  #"), 3, 3, new Vector(3, 2)),
            new Shape(List.of("#", "#", "#", "#"), 4, 1, new Vector(3, 2)),
            new Shape(List.of("##", "##"), 2, 2, new Vector(3, 2)));
    static LinkedList<Direction> parseDirections(String input) {
        return stream(input.split(""))
                .map(character -> character.equals("<") ? Direction.LEFT : Direction.RIGHT)
                .collect(toCollection(LinkedList::new));
    }

    public String ex2(String content) {
        return String.valueOf(simulateRockFalls(parseDirections(content), 1000000000000L));
    }
    record Shape(List<String> s, int height, int width, Vector vector) {
        public long bottom() {
            return vector().row();
        }
        public long top() {
            return bottom() + height() - 1;
        }
        public long left() {
            return vector.column();
        }
        public long right() {
            return left() + width() - 1;
        }
        public Shape move(Direction direction, Cave cave) {
            var nextHeight = vector.row() + direction.height();
            int leftMost = cave.width() - this.width;
            var suggestedCoordinate = vector.column() + direction.right();
            var nextRight = max(0, min(suggestedCoordinate, leftMost));
            var nextCoordinate = new Vector(nextHeight, nextRight);
            return new Shape(s, height, width, nextCoordinate);
        }
        public boolean canMove(Direction direction, Cave cave) {
            return !cave.collides(this.move(direction, cave));
        }
        public boolean exists(Vector target) {
            if (target.row() < bottom()) return false;
            if (target.row() > top()) return false;
            if (target.column() < left()) return false;
            if (target.column() > right()) return false;
            return existsInShapeCoordinate(target.row() - vector.row(), target.column() - vector.column());
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
                        Vector target = new Vector(i, j);
                        sb.replace(j, j + 1, shape.exists(target) || this.exists(target) ? "#" : " ");
                    }
                }
                futureRows.add(sb.toString());
            }
            return new Cave(width, futureRows);
        }

        public boolean collides(Shape shape) {
            return rangeClosed(shape.bottom(), shape.top()).boxed()
                    .flatMap(i -> rangeClosed(shape.left(), shape.right()).mapToObj(j -> new Vector(i, j)))
                    .anyMatch(c -> shape.exists(c) && this.exists(c));
        }
        public int stackHeight() {
            int endIndex = rows.size();
            int fromIndex = max(0, endIndex - 5);
            return (int) rows.subList(fromIndex, endIndex).stream().filter(s -> s.contains("#")).count() + fromIndex;
        }
        public boolean exists(Vector target) {
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