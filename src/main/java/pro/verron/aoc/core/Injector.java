package pro.verron.aoc.core;

import org.jetbrains.annotations.NotNull;
import pro.verron.aoc.utils.functional.ThrowingFunction;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public interface Injector<T> {
    Charset charset = StandardCharsets.UTF_8;
    @SuppressWarnings("rawtypes")
    Map<Class, Injector> injectors = Map.of(
            String.class, string(),
            Stream.class, stream(),
            List.class, list()
    );

    private static Injector<String> string() {
        return (path, delimiter) -> Files.readString(path, charset);
    }

    private static Injector<Stream<String>> stream() {
        return (path, delimiter) -> new Scanner(path, charset)
                .useDelimiter(delimiter)
                .tokens();
    }

    private static Injector<List<String>> list() {
        return stream().then(Stream::toList);
    }

    @NotNull
    static <T> Optional<Injector<T>> find(Class<T> inputType) {
        @SuppressWarnings("unchecked")
        Injector<T> value = injectors.get(inputType);
        return Optional.ofNullable(value);
    }

    T extract(Path path, String delimiter) throws Exception;

    default <U> Injector<U> then(Function<T, U> func) {
        return (path, delimiter) -> func.apply(this.extract(path, delimiter));
    }

    @NotNull
    default ThrowingFunction<Path, Object> withDelimiter(String delimiter) {
        return path -> {
            try {
                return this.extract(path, delimiter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
