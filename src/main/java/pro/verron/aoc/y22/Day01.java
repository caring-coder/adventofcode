package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;

public record Day01(List<Bag> bags) {
    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 1);
        Day01 sampleExercise = new Day01(aoc.testStream("\n\n").map(Day01::parseBag).toList());
        Day01 customExercise = new Day01(aoc.trueStream("\n\n").map(Day01::parseBag).toList());
        assertEquals(sampleExercise.calorieCounting(1), 24000);
        assertEquals(customExercise.calorieCounting(1), 70369);
        assertEquals(sampleExercise.calorieCounting(3), 45000);
        assertEquals(customExercise.calorieCounting(3), 203002);
    }

    int calorieCounting(int nbElfs) {
        return this.bags.stream()
                .sorted(comparing(Day01::calorieTotal).reversed())
                .limit(nbElfs)
                .reduce(0, (acc, bag) -> acc + calorieTotal(bag), Integer::sum);
    }

    private static int calorieTotal(Bag b) {
        return b.snacks.stream().mapToInt(Snack::calorie).sum();
    }

    static Bag parseBag(String strBag) {
        return strBag.lines()
                .map(Day01::parseSnack)
                .reduce(new Bag(emptyList()), Bag::add, Bag::merge);
    }

    static Snack parseSnack(String strSnack){
        return new Snack(parseInt(strSnack));
    }

    record Bag(Collection<Snack> snacks){
        public Bag add(Snack snack) {
            return new Bag(Stream.of(this.snacks, List.of(snack)).flatMap(Collection::stream).toList());
        }

        public Bag merge(Bag that) {
            return new Bag(Stream.of(this.snacks, that.snacks).flatMap(Collection::stream).toList());
        }
    }

    record Snack(int calorie){}
}
