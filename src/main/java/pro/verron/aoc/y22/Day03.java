package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.stream.IntStream.range;
import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;

public class Day03 {
    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 3);
        assertEquals(rucksackReorganization(aoc.testList(), Day03::extractPockets), 157, "Sample Part 1");
        assertEquals(rucksackReorganization(aoc.trueList(), Day03::extractPockets), 7742, "Exercice Part 1");
        assertEquals(rucksackReorganization(aoc.testList(), Day03::extractTeam), 70, "Sample Part 2");
        assertEquals(rucksackReorganization(aoc.trueList(), Day03::extractTeam), 2276, "Exercice Part 2");
    }

    @FunctionalInterface
    interface RucksackExtractor{
        List<String> extract(BlockingQueue<String> queue);
    }

    private static String intersect(String first, String second) {
        return range(0, first.length())
                .mapToObj(first::charAt)
                .filter(c -> second.indexOf(c) >= 0)
                .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private static String intersect(Collection<String> elements) {
        return elements.stream().reduce(Day03::intersect).orElse("");
    }

    private static int rucksackReorganization(List<String> rucksacks, RucksackExtractor extractor) {
        int sum = 0;
        var rucksacksQueue = new LinkedBlockingQueue<>(rucksacks);
        while(!rucksacksQueue.isEmpty()) {
            List<String> team = extractor.extract(rucksacksQueue);
            Character common = intersect(team).charAt(0);
            sum += computePriority(common);
        }
        return sum;
    }

    private static List<String> extractPockets(BlockingQueue<String> rucksacksQueue) {
        String rucksack = rucksacksQueue.remove();
        String pocket1 = rucksack.substring(0, rucksack.length() / 2);
        String pocket2 = rucksack.substring(rucksack.length() / 2);
        return List.of(pocket1, pocket2);
    }

    private static List<String> extractTeam(BlockingQueue<String> rucksacksQueue) {
        List<String> team = new ArrayList<>();
        rucksacksQueue.drainTo(team, 3);
        return team;
    }

    private static int computePriority(Character item) {
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(item) + 1;
    }
}
