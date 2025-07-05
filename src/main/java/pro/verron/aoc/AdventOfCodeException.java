package pro.verron.aoc;

public class AdventOfCodeException
        extends RuntimeException {
    public AdventOfCodeException(Exception exception) {
        super(exception);
    }
}
