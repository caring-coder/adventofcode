package pro.verron.aoc.utils;

public record Position(int x, int y) {
    public Position east() {
        return new Position(x, y + 1);
    }

    public Position north() {
        return new Position(x + 1, y);
    }

    public Position west() {
        return new Position(x, y - 1);
    }

    public Position south() {
        return new Position(x - 1, y);
    }
}
