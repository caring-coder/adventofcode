package pro.verron.aoc.utils.board;

@FunctionalInterface
public interface Setter<T> {
    void set(int index, T value);
}
