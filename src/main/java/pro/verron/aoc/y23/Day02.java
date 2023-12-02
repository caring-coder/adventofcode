package pro.verron.aoc.y23;


import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Day02 {
    private static Game parseGame(String string) {
        return new Game(string);
    }

    public String ex1(Stream<String> content) {
        int sum = content.map(Day02::parseGame)
                .filter(Game.isValid(12, 13, 14))
                .mapToInt(Game::no)
                .sum();
        return String.valueOf(sum);
    }

    public String ex2(Stream<String> content) {
        int sum = content.map(Day02::parseGame)
                .mapToInt(Game::power)
                .sum();
        return String.valueOf(sum);
    }

    private record Game(int no, List<Draw> draws) {
        public Game(String string) {
            this(Integer.parseInt(string.split(":")[0].split(" ")[1]),
                 Stream.of(string.split(":")[1].split(";"))
                         .map(Draw::new)
                         .toList());
        }

        public static Predicate<? super Game> isValid(
                int red, int green, int blue
        ) {
            return (game) -> game.maxRed() <= red
                             && game.maxGreen() <= green
                             && game.maxBlue() <= blue;
        }

        private int maxBlue() {
            return draws.stream()
                    .mapToInt(Draw::blue)
                    .max()
                    .orElse(0);
        }

        private int maxGreen() {
            return draws.stream()
                    .mapToInt(Draw::green)
                    .max()
                    .orElse(0);
        }

        private int maxRed() {
            return draws.stream()
                    .mapToInt(Draw::red)
                    .max()
                    .orElse(0);
        }

        public int power() {
            return maxBlue() * maxGreen() * maxRed();
        }
    }


    private record Draw(int red, int green, int blue) {
        Draw(String string) {
            this(Stream.of(string.split(","))
                         .map(String::trim)
                         .map(s1 -> s1.split(" "))
                         .collect(toMap(strings -> strings[1],
                                        strings -> Integer.parseInt(strings[0]))));
        }

        Draw(Map<String, Integer> map) {
            this(map.getOrDefault("red", 0),
                 map.getOrDefault("green", 0),
                 map.getOrDefault("blue", 0));
        }
    }
}
