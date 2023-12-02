package pro.verron.aoc.utils.board;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public record Board<T>(int height, int width, Getter<T> getter, Setter<T> setter) {

    public Board(int height, int width, Getter<T> getter){
        this(height, width, getter, null);
    }

    public int size() {
        return height() * width();
    }

    public Set<Square<T>> findAll(T v) {
        return squares().filter(s->s.value().equals(v)).collect(Collectors.toSet());
    }

    public Square<T> find(T v) {
        return squares().filter(s->s.value().equals(v)).findFirst().orElseThrow();
    }

    public Square<T> square(int index){
        return new Square<>(index, this);
    }

    public Stream<Square<T>> squares() {
        return range(0, height * width)
                .mapToObj(this::square);
    }

    public Optional<Square<T>> next(Square<T> square, Direction direction) {
        return next(square.index(), direction);
    }

    private Optional<Square<T>> next(int index, Direction direction) {
        int height = index / width;
        int right = index % width;
        height += direction.height();
        right += direction.right();
        if(0 > height || height >= this.height) return Optional.empty();
        if(0 > right || right >= width) return Optional.empty();
        return Optional.of(new Square<>((height * width) + (right % width), this));
    }
    public boolean contains(int index) {
        return 0 <= index && index < height * width;
    }
}
