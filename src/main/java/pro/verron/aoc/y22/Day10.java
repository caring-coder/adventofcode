package pro.verron.aoc.y22;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static pro.verron.aoc.y22.Day10.Operation.ADDX;
import static pro.verron.aoc.y22.Day10.Operation.NOOP;

public class Day10 {
    public String ex1(Stream<String> content) {
        return cathodeRayTube(content, Day10::signalStrength).toString();
    }

    public String ex2(Stream<String> content) {
        return cathodeRayTube(content, Day10::crtDisplay);

    }
    private static String crtDisplay(int[] integers) {
        return IntStream.range(0, 40 * 6)
                .mapToObj(i -> abs(i % 40 - integers[i]) <= 1)
                .map(b -> b ? "█":".")
                .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
                .insert(200, "\n")
                .insert(160, "\n")
                .insert(120, "\n")
                .insert(80, "\n")
                .insert(40, "\n")
                .toString();
    }

    private static int signalStrength(int[] integers) {
        return registerAtCycle(20, integers)
               + registerAtCycle(60, integers)
               + registerAtCycle(100, integers)
               + registerAtCycle(140, integers)
               + registerAtCycle(180, integers)
               + registerAtCycle(220, integers);
    }

    private <T> T cathodeRayTube(Stream<String> input, Function<int[], T> signalStrength) {
        AtomicInteger register = new AtomicInteger(1);
        int[] integers = input.map(Instruction::parse)
                .flatMap(Instruction::effects)
                .mapToInt(register::getAndUpdate)
                .toArray();
        return signalStrength.apply(integers);
    }

    private static int registerAtCycle(int index, int[] integers1) {
        return integers1[index - 1] * index;
    }

    record Instruction(Operation op, int value){
        private static Instruction parse(String line) {
            return switch (line) {
                case String s when s.equals("noop") -> new Instruction(NOOP, 0);
                case String s when s.startsWith("addx ") -> new Instruction(ADDX, parseInt(s.substring(5)));
                default -> throw new IllegalStateException("Unexpected value: " + line);
            };
        }

        public Stream<IntUnaryOperator> effects() {
            return switch (op){
                case NOOP -> Stream.of(i -> i);
                case ADDX -> Stream.of(i -> i, i -> i + value);
            };
        }
    }
    enum Operation{ NOOP, ADDX }
}