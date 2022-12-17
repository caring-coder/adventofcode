package pro.verron.aoc.y22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static java.util.stream.Collectors.joining;

public class Day5 {

    public static void main(String[] args) throws IOException {
        Path source = Path.of("input","y22", "day5-input.txt");
        out.println(ex1(Files.readString(source), (l, m) -> m.apply9000(l)));
        out.println(ex1(Files.readString(source), (l, m) -> m.apply9001(l)));
    }
    private static Object ex1(String crates, BiConsumer<List<Deque<Character>>, Move> moveConsumer) {
        String[] split = crates.split("\n\n");
        String table = split[0];
        String[] rows = table.split("\n");
        rows = Arrays.copyOf(rows, rows.length -1);
        List<Deque<Character>> l = new ArrayList<>();

        for (int i = rows.length - 1; i >= 0; i--) {
            for(int j = 1; j < rows[i].length(); j = j + 4) {
                int idx = (j - 1) / 4;
                char value = rows[i].charAt(j);
                if(l.size() <= idx) l.add(new ArrayDeque<>());
                if(' ' != value) l.get(idx).push(value);
            }
        }

        Arrays.stream(split[1].split("\n"))
                .map(s -> s.split(" "))
                .map(a -> new Move(a[1], a[3], a[5]))
                .forEach(m -> moveConsumer.accept(l, m));
        return l.stream()
                .map(Deque::peek)
                .map(String::valueOf)
                .collect(joining());
    }

    private record Move(int nb, int from, int to) {
        public Move(String nb, String from, String to){
            this(parseInt(nb), parseInt(from), parseInt(to));
        }
        public void apply9000(List<Deque<Character>> l) {
            for (int i = 0; i < nb; i++) {
                l.get(to-1).push(l.get(from -1).pop());
            }
        }

        public void apply9001(List<Deque<Character>> l) {
            Deque<Character> move = new ArrayDeque<>();
            for (int i = 0; i < nb; i++) {
                move.push(l.get(from -1).pop());
            }
            for (int i = 0; i < nb; i++) {
                l.get(to-1).push(move.pop());
            }
        }
    }
}
