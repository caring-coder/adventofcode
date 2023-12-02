package pro.verron.aoc.utils.board;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Stream.iterate;
import static pro.verron.aoc.utils.board.Direction.*;

public record Square<T>(int index, Board<T> board) {
    public static final Direction[] URDL = {UP, RIGHT, DOWN, LEFT};

    public Stream<Square<T>> lineOfSight(Direction direction) {
        return iterate(
                board.next(this, direction),
                Optional::isPresent,
                square -> board.next(square.orElseThrow(), direction)
        ).flatMap(Optional::stream);
    }

    public T value() {
        return board.getter().get(index);
    }

    public void set(T v) {
        board.setter().set(index, v);
    }

    public List<Square<T>> neighbours() {
        return Stream.of(URDL)
                .map(d -> board.next(this, d))
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public String toString() {
        return "(" + row() + ", " + column() + ", " + value() + ")";
    }

    private int column() {
        return index % board.width();
    }

    private int row() {
        return index / board.width();
    }
}
