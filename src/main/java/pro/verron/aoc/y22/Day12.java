package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;
import pro.verron.aoc.utils.board.Board;
import pro.verron.aoc.utils.board.Square;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;

public record Day12(Board<Character> board, Square<Character> start, Square<Character> end) {
    public Day12(Board<Character> board, char startChar, char endChar) {
        this(board, board.find(startChar), board.find(endChar));
        start.set('a');
        end.set('z');
    }

    private static Board<Character> parseBoard(List<String> input) {
        int[] heights = input.stream().flatMapToInt(String::chars).toArray();
        int height = input.size();
        int length = input.get(0).length();
        return new Board<>(height, length, i -> (char) heights[i], (i, v)-> heights[i] = v);
    }

    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 12);
        Day12 sample = new Day12(parseBoard(aoc.testList()), 'S', 'E');
        Day12 custom = new Day12(parseBoard(aoc.trueList()), 'S', 'E');
        assertEquals(sample.hillClimbingAlgorithm(sample.end, sample.start), 31);
        assertEquals(custom.hillClimbingAlgorithm(custom.end, custom.start), 437);
        assertEquals(sample.hillClimbingAlgorithm(sample.end, sample.board.findAll('a')), 29);
        assertEquals(custom.hillClimbingAlgorithm(custom.end, custom.board.findAll('a')), 430);
    }

    private long hillClimbingAlgorithm(Square<Character> from, Set<Square<Character>> to) {
        return shortestDistance(from, to, board.height() * board.width());
    }
    private long hillClimbingAlgorithm(Square<Character> from, Square<Character> to) {
        return shortestDistance(from, Set.of(to), board.height() * board.width());
    }

    private static Integer shortestDistance(Square<Character> start, Set<Square<Character>> end, int sizeHint) {
        Map<Square<Character>, Integer> map = HashMap.newHashMap(sizeHint);
        end.forEach(e -> map.put(e, 0));
        Queue<Square<Character>> q = new LinkedList<>(end);
        while (!map.containsKey(start)){
            var current = q.remove();
            var currentDistance = map.get(current);
            Consumer<Square<Character>> consumer = s -> map.put(s, currentDistance + 1);
            current.neighbours()
                    .filter(n -> n.value() <= current.value() + 1)
                    .filter(n -> map.getOrDefault(n, Integer.MAX_VALUE) > currentDistance + 1)
                    .forEach(consumer.andThen(q::add));
        }
        return map.get(start);
    }
}