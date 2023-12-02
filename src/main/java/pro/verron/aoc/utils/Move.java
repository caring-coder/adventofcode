package pro.verron.aoc.utils;

import java.util.function.IntFunction;

public enum Move {
    UPPER(+1), LOWER(-1);

    private final int value;

    Move(int value) {this.value = value;}

    public static IntFunction<Move> parser(char upper, char lower) {
        return ch -> {
            if (upper == ch) return Move.UPPER;
            if (lower == ch) return Move.LOWER;
            throw new IllegalStateException("Unexpected value: " + ch);
        };
    }

    public int value() {
        return value;
    }
}
