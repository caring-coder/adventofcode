package pro.verron.aoc.utils.board;

import static java.lang.Long.signum;

public record Vector(long row, long column) {
    public boolean isNormalized() {
        return row == signum(row) && column == signum(column);
    }

    public Vector add(Vector vector) {
        return new Vector(row() + vector.row(), column() + vector.column());
    }

    public Vector minus(Vector vector) {
        return new Vector(row() - vector.row(), column() - vector.column());
    }

    public Vector normalized() {
        return new Vector(signum(row), signum(column));
    }
}
