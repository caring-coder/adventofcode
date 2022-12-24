package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;
import pro.verron.aoc.utils.board.Board;
import pro.verron.aoc.utils.board.ScoreComputer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;
import static pro.verron.aoc.utils.board.Direction.*;

public class Day08 {
    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 8);
        assertEquals(treetopTreeHouse(aoc.testList(), Day08::nbVisibleTrees), 21L, "Sample Part 1");
        assertEquals(treetopTreeHouse(aoc.trueList(), Day08::nbVisibleTrees), 1679L, "Exercice Part 1");
        assertEquals(treetopTreeHouse(aoc.testList(), Day08::scenicScore), 8L, "Sample Part 2");
        assertEquals(treetopTreeHouse(aoc.trueList(), Day08::scenicScore), 536625L, "Exercice Part 2");
    }

    private static long treetopTreeHouse(List<String> input, ScoreComputer<Integer> scorer) {
        int[] treeSizes = input.stream().flatMapToInt(String::chars).map(Character::getNumericValue).toArray();
        int height = input.size();
        int length = input.get(0).length();
        Board<Integer> board = new Board<>(height, length, i -> treeSizes[i]);
        return scorer.computeScore(board);
    }

    private static long nbVisibleTrees(Board<Integer> board) {
        return board.squares()
                .filter(square -> Stream.of(UP, RIGHT, DOWN, LEFT)
                        .map(square::lineOfSight)
                        .map(otherSquare -> otherSquare.noneMatch(other -> hides(other, square)))
                        .reduce(false, Boolean::logicalOr))
                .count();
    }

    private static long scenicScore(Board<Integer> board) {
        return board.squares()
                .mapToLong(square -> Stream.of(UP, RIGHT, DOWN, LEFT)
                        .map(square::lineOfSight)
                        .map(los -> count(los, other -> hides(other, square)))
                        .reduce(1L, (acc, current) -> acc * current)
                ).max()
                .orElse(0L);
    }

    private static <T> long count(Stream<T> los, Predicate<T> stopMarker) {
        AtomicBoolean stop = new AtomicBoolean(false);
        return los.map(stopMarker::test).takeWhile(hides -> !stop.getAndSet(hides)).count();
    }

    private static boolean hides(Board<Integer>.Square other, Board<Integer>.Square that) {
        return that.value() <= other.value();
    }
}