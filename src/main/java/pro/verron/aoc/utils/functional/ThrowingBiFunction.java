package pro.verron.aoc.utils.functional;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ThrowingBiFunction<I, J, O> {
    static <I, J, O> BiFunction<I, J, O> sneaky(ThrowingBiFunction<I, J, O> biFunction) {
        return (i, j) -> {
            try {
                return biFunction.apply(i, j);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    O apply(I i, J j) throws Exception;
}
