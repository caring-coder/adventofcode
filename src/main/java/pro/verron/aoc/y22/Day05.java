package pro.verron.aoc.y22;

import java.util.*;

import static java.lang.Integer.parseInt;
import static java.util.Collections.asLifoQueue;
import static java.util.stream.Collectors.joining;

public class Day05 {
    public String ex1(String content) {
        return String.valueOf(supplyStacks(content, Move::apply9000));
    }

    public String ex2(String content) {
        return String.valueOf(supplyStacks(content, Move::apply9001));
    }
    interface CrateMover {
        void apply(Move move, List<Queue<Character>> crateStacks);
    }

    private static Object supplyStacks(String input, CrateMover mover) {
        String[] split = input.split("\n\n");

        List<Queue<Character>> l = computeStacks(split[0]);

        Arrays.stream(split[1].split("\n"))
                .map(s -> s.split(" "))
                .map(a -> new Move(a[1], a[3], a[5]))
                .forEach(m -> mover.apply(m, l));

        return l.stream()
                .map(Queue::peek)
                .map(String::valueOf)
                .collect(joining());
    }

    private static List<Queue<Character>> computeStacks(String table) {
        String[] rows = table.split("\n");
        rows = Arrays.copyOf(rows, rows.length - 1);
        List<Queue<Character>> stacks = new ArrayList<>();

        for (int i = rows.length - 1; i >= 0; i--) {
            for (int j = 1; j < rows[i].length(); j = j + 4) {
                int idx = (j - 1) / 4;
                char value = rows[i].charAt(j);
                if (stacks.size() <= idx) stacks.add(asLifoQueue(new ArrayDeque<>()));
                if (' ' != value) stacks.get(idx).add(value);
            }
        }
        return stacks;
    }

    record Move(int nb, int from, int to) {
        public Move(String nb, String from, String to) {
            this(parseInt(nb), parseInt(from), parseInt(to));
        }

        public void apply9000(List<Queue<Character>> stack) {
            for (int i = 0; i < nb; i++) {
                stack.get(to - 1).add(stack.get(from - 1).poll());
            }
        }

        public void apply9001(List<Queue<Character>> stack) {
            Deque<Character> move = new ArrayDeque<>();
            for (int i = 0; i < nb; i++) {
                move.push(stack.get(from - 1).poll());
            }
            for (int i = 0; i < nb; i++) {
                stack.get(to - 1).add(move.pop());
            }
        }
    }
}