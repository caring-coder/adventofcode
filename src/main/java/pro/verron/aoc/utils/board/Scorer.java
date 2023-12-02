package pro.verron.aoc.utils.board;

public interface Scorer<T> {
    long score(Board<T> board);
}
