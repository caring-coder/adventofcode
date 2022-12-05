package pro.verron.aoc.y22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static pro.verron.aoc.y22.Day2.Play.*;
import static pro.verron.aoc.y22.Day2.State.*;

public class Day2 {
    public static void main(String[] args) throws IOException {
        Path source = Path.of("input","y22", "day2-input.txt");
        String content = Files.readString(source);
        out.println(strategy1(content));
        out.println(strategy2(content));
    }

    private static String strategy1(String content) {
        return stream(content.split("\n"))
                .map(line -> line.split(" "))
                .map(line -> new Game(parsePlay(line[0], "A", "B", "C"), parsePlay(line[1], "X", "Y", "Z")))
                .map(Game::value)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No strategy");
    }

    private static String strategy2(String content) {
        return stream(content.split("\n"))
                .map(line -> line.split(" "))
                .map(line -> new Game(parsePlay(line[0], "A", "B", "C"), parseIntent(line[1], "X", "Y", "Z")))
                .map(Game::value)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No strategy");
    }

    private static State parseIntent(final String token, final String loss, final String draw, final String win) {
        if (token.equals(loss)) return LOSS;
        else if (token.equals(draw)) return DRAW;
        else if (token.equals(win)) return WIN;
        throw new IllegalStateException("Unexpected value: " + token);
    }

    private static Play parsePlay(final String token, String rock, String paper, String scissor) {
        if (token.equals(rock)) return ROCK;
        else if (token.equals(paper)) return PAPER;
        else if (token.equals(scissor)) return SCISSOR;
        throw new IllegalStateException("Unexpected value: " + token);
    }

    public enum Play {ROCK, PAPER, SCISSOR}

    public enum State {WIN, LOSS, DRAW}

    public record Game(Play elfPlay, Play myPlay, State state) {

        public static final Map<Play, Map<Play, State>> STATE_COMPUTER = Map.of(
                ROCK, Map.of(ROCK, DRAW, PAPER, WIN, SCISSOR, LOSS),
                PAPER, Map.of(ROCK, LOSS, PAPER, DRAW, SCISSOR, WIN),
                SCISSOR, Map.of(ROCK, WIN, PAPER, LOSS, SCISSOR, DRAW)
        );
        public static final Map<Play, Map<State, Play>> PLAY_COMPUTER = Map.of(
                ROCK, Map.of(DRAW, ROCK, WIN, PAPER, LOSS, SCISSOR),
                PAPER, Map.of(LOSS, ROCK, DRAW, PAPER, WIN, SCISSOR),
                SCISSOR, Map.of(WIN, ROCK, LOSS, PAPER, DRAW, SCISSOR));

        Game(Play elfPlay, Play myPlay) {
            this(elfPlay, myPlay, STATE_COMPUTER.get(elfPlay).get(myPlay));
        }

        Game(Play elfPlay, State state) {
            this(elfPlay, PLAY_COMPUTER.get(elfPlay).get(state), state);
        }

        public int value() {
            int stateScore = Map.of(LOSS, 0, DRAW, 3, WIN, 6).get(state);
            int myPlayScore = Map.of(ROCK, 1, PAPER, 2, SCISSOR, 3).get(myPlay);
            return stateScore + myPlayScore;
        }
    }
}
