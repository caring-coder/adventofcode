package pro.verron.aoc.y15;

import static java.util.Arrays.stream;

public class Day01 {
    public String ex1(String content) {
        return stream(content.split(""))
                .map(chr -> chr.equals("(") ? 1 : -1)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No elfs found");
    }

    public String ex2(String content) {
        int floor = 0;
        for (int i = 0; i < content.length(); i++) {
            floor += content.charAt(i) == '(' ? 1 : -1;
            if (floor == -1)
                return String.valueOf(i + 1);
        }
        return "Never";
    }
}
