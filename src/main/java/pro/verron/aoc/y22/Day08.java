package pro.verron.aoc.y22;

import pro.verron.aoc.utils.board.Board;
import pro.verron.aoc.utils.board.Scorer;
import pro.verron.aoc.utils.board.Square;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static pro.verron.aoc.utils.board.Direction.*;

public class Day08 {
    public String ex1(List<String> content) {
        return String.valueOf(treetopTreeHouse(content, Day08::nbVisibleTrees));
    }

    public String ex2(List<String> content) {
        return String.valueOf(treetopTreeHouse(content, Day08::scenicScore));
    }

    private static long treetopTreeHouse(
            List<String> input,
            Scorer<Integer> scorer
    ) {
        int[] treeSizes = input.stream().flatMapToInt(String::chars).map(Character::getNumericValue).toArray();
        int height = input.size();
        int length = input.get(0).length();
        Board<Integer> board = new Board<>(height, length, i -> treeSizes[i]);
        return scorer.score(board);
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

    private static boolean hides(Square<Integer> other, Square<Integer> that) {
        return that.value() <= other.value();
    }
}
