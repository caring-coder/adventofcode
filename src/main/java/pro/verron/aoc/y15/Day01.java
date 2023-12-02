package pro.verron.aoc.y15;

import pro.verron.aoc.utils.Move;
import pro.verron.aoc.utils.Moves;
import pro.verron.aoc.utils.monad.Monad;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Day01 {

    public String ex1(String content) {
        return content.chars()
                .mapToObj(Move.parser('(', ')'))
                .reduce(Monad.monadOf(new Moves()),
                        Monad.accumulate(Moves::add),
                        Monad.combine(Moves::merge))
                .map(Moves::floor)
                .map(String::valueOf)
                .get();
    }

    public String ex2(String content) {
        AtomicReference<Moves> ref = new AtomicReference<>(new Moves());
        return content.chars()
                .mapToObj(Move.parser('(', ')'))
                .map(move -> ref.updateAndGet(state -> state.add(move)))
                .filter(state -> state.floor() == -1)
                .findFirst()
                .map(Moves::moves)
                .map(List::size)
                .map(String::valueOf)
                .orElse("Never");
    }
}
