package pro.verron.aoc.utils.functional;

import pro.verron.aoc.AdventOfCodeException;

import java.util.function.Function;


@FunctionalInterface
public interface ThrowingFunction<I, O> {
    static <I, O> Function<I, O> sneaky(ThrowingFunction<I, O> function) {
        return i -> {
            try {
                return function.apply(i);
            } catch (Exception e) {
                throw new AdventOfCodeException(e);
            }
        };
    }

    O apply(I input) throws AdventOfCodeException;
}
