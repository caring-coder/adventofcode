package pro.verron.aoc.utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public record Moves(List<Move> moves, int floor) {
    public Moves() {
        this(emptyList(), 0);
    }

    public Moves(List<Move> moves) {
        this(moves, moves.stream()
                .mapToInt(Move::value)
                .sum());
    }

    public Moves(List<Move> moves, int floor) {
        this.moves = List.copyOf(moves);
        this.floor = floor;
    }

    public Moves add(Move move) {
        var set = new ArrayList<>(moves);
        set.add(move);
        return new Moves(set);
    }

    public Moves merge(Moves that) {
        var set = new ArrayList<>(this.moves);
        set.addAll(that.moves);
        return new Moves(set);
    }
}
