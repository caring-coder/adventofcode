package pro.verron.aoc.y22;

import pro.verron.aoc.utils.board.Direction;
import pro.verron.aoc.utils.board.Vector;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;

public class Day09 {
    public String ex1(Stream<String> content) {
        return String.valueOf(ropeBridge(content, 2));
    }

    public String ex2(Stream<String> content) {
        return String.valueOf(ropeBridge(content, 10));
    }

    private int ropeBridge(Stream<String> input, int nbKnots) {
        assert nbKnots >= 2 : "The rope cannot have less than two ends and zero intermediate knots";
        var pattern = Pattern.compile("([RULD]) (\\d+)");
        var startPosition = new Vector(0, 0);
        var rope = new Rope(nbKnots, startPosition);
        var places = new HashSet<>(Set.of(startPosition));
        input.map(pattern::matcher)
                .filter(Matcher::matches)
                .flatMap(Day09::parseInputLines)
                .map(Day09::parseDirection)
                .map(rope::moveHead)
                .map(Rope::getTail)
                .forEachOrdered(places::add);
        return places.size();
    }

    private static Stream<String> parseInputLines(Matcher m) {
        var directionCharacter = m.group(1);
        var directionValue = parseInt(m.group(2));
        return directionCharacter
                .repeat(directionValue)
                .chars()
                .mapToObj(Character::toString);
    }

    public static class Rope {
        private final LinkedList<Vector> knots;
        public Rope(int nbKnots, Vector startPosition){
            knots = range(0, nbKnots)
                    .mapToObj(i -> startPosition)
                    .collect(toCollection(LinkedList::new));
        }
        public Rope moveHead(Direction direction) {
            var head = knots.getFirst();
            knots.set(0, direction.from(head));
            for (int i = 1; i < knots.size(); i++) {
                var prev = knots.get(i - 1);
                var curr = knots.get(i);
                var delta = prev.minus(curr);
                if(!delta.isNormalized()) {
                    knots.set(i, curr.add(delta.normalized()));
                }
            }
            return this;
        }
        public Vector getTail() {
            return knots.getLast();
        }
    }

    private static Direction parseDirection(String s) {
        return switch (s) {
            case "R" -> Direction.RIGHT;
            case "U" -> Direction.UP;
            case "L" -> Direction.LEFT;
            case "D" -> Direction.DOWN;
            default -> throw new RuntimeException();
        };
    }
}