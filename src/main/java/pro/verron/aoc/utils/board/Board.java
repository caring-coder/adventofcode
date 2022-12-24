package pro.verron.aoc.utils.board;

import java.util.OptionalInt;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.iterate;

public record Board<T>(int height, int width, IntFunction<T> valuation) {




    public class Square {
        private final int index;
        public Square(int index) {
            this.index = index;
        }
        public Stream<Square> lineOfSight(Direction direction) {
            return iterate(next(index, direction), OptionalInt::isPresent, i -> next(i.orElseThrow(), direction))
                    .mapToInt(OptionalInt::orElseThrow)
                    .mapToObj(Square::new);
        }
        public T value(){
            return valuation.apply(index);
        }
    }
    public Square square(int index){
        return new Square(index);
    }

    public Stream<Board<T>.Square> squares() {
        return range(0, height * width)
                .mapToObj(this::square);
    }

    public OptionalInt next(int index, Direction direction) {
        int height = index / width;
        int right = index % width;
        height += direction.height();
        right += direction.right();
        if(0 > height || height >= this.height) return OptionalInt.empty();
        if(0 > right || right >= width) return OptionalInt.empty();
        return OptionalInt.of((height * width) + (right % width));
    }
    public boolean contains(int index) {
        return 0 <= index && index < height * width;
    }
}