package pro.verron.aoc.y22;

import pro.verron.aoc.core.Delimiter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;

public class Day01 {

    @Delimiter("\n\n")
    public String ex1(Stream<String> content) {
        List<Bag> bags = content.map(Day01::parseBag).toList();
        int nbElfs = 1;
        return bags.stream()
                .sorted(comparing(Day01::calorieTotal).reversed())
                .limit(nbElfs)
                .reduce(0, (acc, bag) -> acc + calorieTotal(bag), Integer::sum)
                .toString();
    }

    @Delimiter("\n\n")
    public String ex2(Stream<String> content) {
        List<Bag> bags = content.map(Day01::parseBag).toList();
        int nbElfs = 3;
        return bags.stream()
                .sorted(comparing(Day01::calorieTotal).reversed())
                .limit(nbElfs)
                .reduce(0, (acc, bag) -> acc + calorieTotal(bag), Integer::sum)
                .toString();
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
