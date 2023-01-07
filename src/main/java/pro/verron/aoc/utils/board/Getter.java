package pro.verron.aoc.utils.board;

@FunctionalInterface
public interface Getter<T> {
    T get(int index);
}
