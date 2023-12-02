package pro.verron.aoc.y22;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {
    static Stream<Arguments> ex1sources() {
        return Stream.of(
                Arguments.of("SE", "1"),
                Arguments.of("SaE", "2"),
                Arguments.of("SaaE", "3"),
                Arguments.of("SaaaE", "4"),
                Arguments.of("SabcE", "4"),
                Arguments.of("SacaE", "No path found"),
                Arguments.of("aaaaa\nSaaaE", "4"),
                Arguments.of("SaaaE\naaaaa", "4"),
                Arguments.of("aaaaa\nSacaE", "6"),
                Arguments.of("SacaE\naaaaa", "6")
        );
    }

    @ParameterizedTest
    @MethodSource("ex1sources")
    void ex1(String in, String expected) {
        List<String> list = Arrays.asList(in.split("\n"));
        var actual = new Day12().ex1(list);
        assertEquals(expected, actual);
    }
}
