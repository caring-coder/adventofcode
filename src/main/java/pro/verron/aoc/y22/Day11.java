package pro.verron.aoc.y22;

import pro.verron.aoc.core.Delimiter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.LongPredicate;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

public class Day11 {
    @Delimiter("\n\n")
    public String ex1(Stream<String> content) {
        return String.valueOf(monkeyInTheMiddle(content, 20, 3));
    }

    @Delimiter("\n\n")
    public String ex2(Stream<String> content) {
        return String.valueOf(monkeyInTheMiddle(content, 10_000, 1));
    }

    private long monkeyInTheMiddle(Stream<String> input, int nbRound, int reliefFactor) {
        Map<Integer, Monkey> monkeys = input.map(Monkey::parse).collect(toMap(m -> m.id, m -> m));
        Long mod_all = monkeys.values().stream().map(Monkey::test).map(Test::test).map(DivisibleBy::i).reduce(1L, (op1, op2) -> op1 * op2);
        Map<Integer, AtomicLong> counter = new HashMap<>();
        Consumer<Monkey> countManipulations = monkeyId -> counter.computeIfAbsent(monkeyId.id(), id -> new AtomicLong()).getAndIncrement();
        for (int i = 0; i < nbRound; i++) {
            for (int mId = 0; mId < monkeys.size(); mId++) {
                monkeys.get(mId).compute(reliefFactor, monkeys, countManipulations, mod_all);
            }
        }
        return counter.values()
                .stream()
                .map(AtomicLong::get)
                .sorted(reverseOrder())
                .limit(2)
                .reduce((op1, op2) -> op1 * op2)
                .orElseThrow();
    }

    interface Operator {
        Operator MULT = Math::multiplyExact;
        Operator ADD = Math::addExact;

        static Operator parse(String str) {
            return switch (str) {
                case "*" -> MULT;
                case "+" -> ADD;
                default -> throw new IllegalStateException("Unexpected value: " + str);
            };
        }

        Long apply(Long op1, Long op2);
    }

    private record Item(long worryLevel) {
        public static Item parse(String str) {
            return new Item(parseLong(str));
        }
    }

    @FunctionalInterface
    private interface Operand<T> {
        static Operand<Long> parse(String str) {
            return switch (str) {
                case "old" -> Previous.PREVIOUS;
                case String s when s.matches("\\d+") -> new Value(parseLong(s));
                default -> throw new IllegalStateException("Unexpected value: " + str);
            };
        }

        T evaluate(T old);

        record Value(Long value) implements Operand<Long> {
            @Override
            public Long evaluate(Long old) {
                return value;
            }
        }

        enum Previous implements Operand<Long> {
            PREVIOUS;

            @Override
            public Long evaluate(Long old) {
                return old;
            }
        }
    }

    public record Monkey(int id, Queue<Item> items, Operation operation, Test test) {
        private void compute(int reliefFactor, Map<Integer, Monkey> monkeys, Consumer<Monkey> onManipulate, Long mod_all) {
            while (!this.items.isEmpty()) {
                Item item = this.items.remove();
                onManipulate.accept(this);
                item = this.operation().computeWorriness(item, mod_all);
                item = new Item(item.worryLevel / reliefFactor);
                boolean test = test().test().test(item.worryLevel);
                int nextMonkeyId = test ? this.test.whenTrue : this.test.whenFalse;
                monkeys.get(nextMonkeyId).items.add(item);
            }
        }

        @Override
        public String toString() {
            return "Monkey " + id + ": " + String.join(", ", items.stream().map(item -> item.worryLevel).map(String::valueOf).toList());
        }

        public static Monkey parse(String str) {
            String[] lines = str.split("\n");
            var id = extractId(lines[0].trim());
            var items = extractItems(lines[1].trim());
            var operation = extractOperation(lines[2].trim());
            var test = extractTest(lines[3].trim(), lines[4].trim(), lines[5].trim());
            return new Monkey(id, items, operation, test);
        }

        private static Test extractTest(String line, String line1, String line2) {
            return new Test(line, line1, line2);
        }

        private static Operation extractOperation(String line) {
            String[] split = line.substring("Operation: new = ".length()).split(" ");
            return new Operation(split);
        }

        private static LinkedList<Item> extractItems(String line) {
            return stream(line.substring("Starting items: ".length()).split(", ")).map(String::trim).map(Item::parse).collect(toCollection(LinkedList::new));
        }

        private static int extractId(String line) {
            return parseInt(line.substring("Monkey ".length(), line.lastIndexOf(':')));
        }
    }

    private record Operation(Operand<Long> operand1, Operator operator, Operand<Long> operand2) {
        public Operation(String[] split) {
            this(split[0], split[1], split[2]);
        }

        public Operation(String operand1, String operator, String operand2) {
            this(Operand.parse(operand1), Operator.parse(operator), Operand.parse(operand2));
        }

        public Item computeWorriness(Item item, Long mod_all) {
            return new Item(operator.apply(operand1.evaluate(item.worryLevel), operand2.evaluate(item.worryLevel)) % mod_all);
        }
    }

    private record Test(DivisibleBy test, int whenTrue, int whenFalse) {
        Test(String l1, String l2, String l3) {
            this(new DivisibleBy(parseLong(l1.substring("Test: divisible by ".length()).trim())),
                    parseInt(l2.substring("If true: throw to monkey ".length()).trim()),
                    parseInt(l3.substring("If false: throw to monkey ".length()).trim()));
        }
    }

    private record DivisibleBy(long i) implements LongPredicate {
        @Override
        public boolean test(long v) {
            return v % i == 0;
        }
    }
}