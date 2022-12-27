package pro.verron.aoc.utils.board;

public record Direction(int height, int right) {
    public static final Direction UP = new Direction(1, 0);
    public static final Direction DOWN = new Direction(-1, 0);
    public static final Direction LEFT = new Direction(0, -1);
    public static final Direction RIGHT = new Direction(0, 1);
    public Direction times(int nb) {
        return new Direction(nb * height, nb * right);
    }
    public Vector from(Vector vector) {
        return new Vector(vector.row() + height, vector.column() + right);
    }
}
