package pro.verron.aoc.y15;

public class Day01 {
    public String ex1(String content) {
        var floor = new Floor(0);
        for (char c : content.toCharArray()) {
            floor = switch (c) {
                case '(' -> floor.upper();
                case ')' -> floor.lower();
                default -> throw new IllegalStateException("Unexpected value: " + c);
            };
        }
        return String.valueOf(floor.value());
    }

    public String ex2(String content) {
        var floor = new Floor(0);
        char[] charArray = content.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            floor = switch (c) {
                case '(' -> floor.upper();
                case ')' -> floor.lower();
                default -> throw new IllegalStateException("Unexpected value: " + c);
            };
            if (floor.value() == -1)
                return String.valueOf(i + 1);
        }
        return "Never";
    }

    private record Floor(int value) {
        public Floor upper() {
            return new Floor(value + 1);
        }

        public Floor lower() {
            return new Floor(value - 1);
        }
    }
}


