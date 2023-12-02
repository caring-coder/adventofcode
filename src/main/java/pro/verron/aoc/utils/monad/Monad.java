package pro.verron.aoc.utils.monad;


import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public class Monad<T> {
    private final T object;

    public Monad(T object) {
        this.object = object;
    }

    public static <T> Supplier<Monad<T>> wrap(Supplier<T> supplier) {
        return () -> new Monad<>(supplier.get());
    }

    public static <T, U> BiFunction<Monad<T>, U, Monad<T>> wrap(
            BiFunction<T, U, T> bifunction
    ) {
        return (m, u) -> new Monad<>(bifunction.apply(m.object, u));
    }

    public static <T, U> BiConsumer<Monad<T>, U> wrap(
            BiConsumer<T, U> biConsumer
    ) {
        return (m, u) -> biConsumer.accept(m.object, u);
    }

    public static <T> Consumer<Monad<T>> wrap(Consumer<T> consumer) {
        return monad -> consumer.accept(monad.object);
    }

    @NotNull
    public static <T, U> BiFunction<Monad<T>, U, Monad<T>> accumulate(BiFunction<T, U, T> biFunction) {
        return (Monad<T> s, U move) -> new Monad<>(
                biFunction.apply(s.get(), move));
    }

    @NotNull
    public static <T> BinaryOperator<Monad<T>> combine(BinaryOperator<T> combiner) {
        return (m1, m2) -> new Monad<>(combiner.apply(m1.get(), m2.get()));
    }

    @NotNull
    public static <T> Monad<T> monadOf(T object) {
        return new Monad<>(object);
    }


    public T get() {
        return object;
    }

    public <U> Monad<U> map(Function<T, U> function) {
        return new Monad<>(function.apply(object));
    }
}
