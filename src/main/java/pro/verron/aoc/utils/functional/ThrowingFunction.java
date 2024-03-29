package pro.verron.aoc.utils.functional;

import java.util.function.Function;


@FunctionalInterface
public interface ThrowingFunction<I, O> {
    static <I, O> Function<I, O> sneaky(ThrowingFunction<I, O> function) {
        return (i) -> {
            try {
                return function.apply(i);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    O apply(I input) throws Exception;
}
