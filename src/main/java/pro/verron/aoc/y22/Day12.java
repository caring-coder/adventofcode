package pro.verron.aoc.y22;

import pro.verron.aoc.utils.board.Board;
import pro.verron.aoc.utils.board.Getter;
import pro.verron.aoc.utils.board.Setter;
import pro.verron.aoc.utils.board.Square;

import java.util.*;

import static java.lang.Long.MAX_VALUE;

public class Day12 {

    private static Board<Character> parseBoard(List<String> input) {
        var heights = input.stream()
                .flatMapToInt(String::chars)
                .toArray();
        var height = input.size();
        var length = input.get(0)
                .length();
        Getter<Character> characterGetter = i -> (char) heights[i];
        Setter<Character> characterSetter = (i, v) -> heights[i] = v;
        return new Board<>(height, length, characterGetter, characterSetter);
    }

    private static OptionalLong shortestDistance(
            Square<Character> start,
            Set<Square<Character>> ends,
            int sizeHint
    ) {
        var distanceRecorder = new DistanceRecorder(sizeHint);
        ends.forEach(distanceRecorder::initialize);
        var q = new LinkedList<>(ends);
        while (!distanceRecorder.contains(start) && !q.isEmpty()) {
            var current = q.remove();
            var currentDistance = distanceRecorder.of(current);
            current.neighbours()
                    .stream()
                    .filter(n -> n.value() >= current.value() - 1)
                    .filter(n -> distanceRecorder.of(n) > (currentDistance + 1))
                    .forEach(n ->
                             {
                                 distanceRecorder.initialize(n,
                                                             currentDistance + 1);
                                 q.add(n);
                             });
        }
        return distanceRecorder.contains(start)
                ? OptionalLong.of(distanceRecorder.of(start))
                : OptionalLong.empty();
    }

    public String ex1(List<String> content) {
        var board = parseBoard(content);
        var start = board.find('S');
        var end = board.find('E');
        start.set('a');
        end.set('z');
        var l = hillClimbingAlgorithm(board, start, end);
        return l.isPresent()
                ? String.valueOf(l.getAsLong())
                : "No path found";
    }

    public String ex2(List<String> content) {
        var board = parseBoard(content);
        var start = board.find('S');
        var end = board.find('E');
        start.set('a');
        end.set('z');
        var ends = board.findAll('a');
        var l = hillClimbingAlgorithm(board, start, ends);
        return String.valueOf(l);
    }

    private OptionalLong hillClimbingAlgorithm(
            Board<Character> board,
            Square<Character> from,
            Set<Square<Character>> to
    ) {
        return shortestDistance(from, to, board.size());
    }

    private OptionalLong hillClimbingAlgorithm(
            Board<Character> board,
            Square<Character> from,
            Square<Character> to
    ) {
        return hillClimbingAlgorithm(board, from, Set.of(to));
    }

    static class DistanceRecorder {

        private final Map<Square<Character>, Long> map;

        DistanceRecorder(int sizeHint) {
            this.map = HashMap.newHashMap(sizeHint);
        }


        public void initialize(Square<Character> e) {
            initialize(e, 0L);
        }

        public boolean contains(Square<Character> start) {
            return map.containsKey(start);
        }

        public long of(Square<Character> current) {
            return map.getOrDefault(current, MAX_VALUE);
        }

        public void initialize(Square<Character> s, long l) {
            map.putIfAbsent(s, l);
        }
    }
}
