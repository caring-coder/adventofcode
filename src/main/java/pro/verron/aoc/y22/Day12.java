package pro.verron.aoc.y22;

import pro.verron.aoc.utils.board.Board;
import pro.verron.aoc.utils.board.Square;

import java.util.*;
import java.util.function.Consumer;

public class Day12 {
    public String ex1(List<String> content) {
        Board<Character> board = parseBoard(content);
        Square<Character> start = board.find('S');
        Square<Character> end = board.find('E');
        start.set('a');
        end.set('z');
        long l = hillClimbingAlgorithm(board, start, end);
        return String.valueOf(l);
    }

    public String ex2(List<String> content) {
        Board<Character> board = parseBoard(content);
        Square<Character> start = board.find('S');
        Square<Character> end = board.find('E');
        start.set('a');
        end.set('z');
        long l = hillClimbingAlgorithm(board, start, board.findAll('a'));
        return String.valueOf(l);
    }

    private static Board<Character> parseBoard(List<String> input) {
        int[] heights = input.stream().flatMapToInt(String::chars).toArray();
        int height = input.size();
        int length = input.get(0).length();
        return new Board<>(height, length, i -> (char) heights[i], (i, v)-> heights[i] = v);
    }

    private long hillClimbingAlgorithm(Board<Character> board, Square<Character> from, Set<Square<Character>> to) {
        return shortestDistance(from, to, board.height() * board.width());
    }

    private long hillClimbingAlgorithm(Board<Character> board, Square<Character> from, Square<Character> to) {
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