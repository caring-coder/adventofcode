package pro.verron.aoc.utils.board;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Stream.iterate;

public record Square<T>(int index, Board<T> board) {
    public Stream<Square<T>> lineOfSight(Direction direction) {
        return iterate(board.next(index, direction), Optional::isPresent, i -> board.next(i.orElseThrow().index, direction))
                .flatMap(Optional::stream);
    }

    public T value() {
        return board.getter().get(index);
    }

    public void set(T v) {
        board.setter().set(index, v);
    }

    public Stream<Square<T>> neighbours() {
        return Stream.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
                .map(d -> board.next(index, d))
                .flatMap(Optional::stream);
    }
}
