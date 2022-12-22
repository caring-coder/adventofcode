package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static pro.verron.aoc.Assertions.assertEquals;
import static pro.verron.aoc.y22.Day02.Play.*;
import static pro.verron.aoc.y22.Day02.State.*;

public class Day02 {
    public static void main(String[] args) throws IOException {
        AdventOfCode adventOfCode = new AdventOfCode(22, 2);
        assertEquals(rockPaperScissors(adventOfCode.testStream(), Day02::XYZareMoves), 15, "Sample Part 1");
        assertEquals(rockPaperScissors(adventOfCode.trueStream(), Day02::XYZareMoves), 10404, "Exercice Part 1");
        assertEquals(rockPaperScissors(adventOfCode.testStream(), Day02::XYZareIntents), 12, "Sample Part 2");
        assertEquals(rockPaperScissors(adventOfCode.trueStream(), Day02::XYZareIntents), 10334, "Exercice Part 1");
    }

    @FunctionalInterface
    interface GameParser {
        Game parse(String elfToken, String yourToken);

        default Game parse(String line) {
            return parse(line.split(" "));
        }

        default Game parse(String[] tokens) {
            return parse(tokens[0], tokens[1]);
        }
    }

    private static Game XYZareMoves(String elfToken, String yourToken) {
        Function<String, Play> elfMoveParser = parsePlay("A", "B", "C");
        Function<String, Play> yourMoveParser = parsePlay("X", "Y", "Z");
        return new Game(elfMoveParser.apply(elfToken), yourMoveParser.apply(yourToken));
    }

    private static Game XYZareIntents(String elfToken, String yourToken) {
        Function<String, Play> elfMoveParser = parsePlay("A", "B", "C");
        Function<String, State> intentParser = parseIntent("X", "Y", "Z");
        return new Game(elfMoveParser.apply(elfToken), intentParser.apply(yourToken));
    }

    private static Function<String, Play> parsePlay(String rock, String paper, String scissor) {
        return token -> {
            if (token.equals(rock)) return ROCK;
            else if (token.equals(paper)) return PAPER;
            else if (token.equals(scissor)) return SCISSOR;
            throw new IllegalStateException("Unexpected value: " + token);
        };
    }

    private static Function<String, State> parseIntent(String loss, String draw, String win) {
        return token -> {
            if (token.equals(loss)) return LOSS;
            else if (token.equals(draw)) return DRAW;
            else if (token.equals(win)) return WIN;
            throw new IllegalStateException("Unexpected value: " + token);
        };
    }

    private static int rockPaperScissors(Stream<String> content, GameParser gameParser) {
        return content.map(gameParser::parse).mapToInt(Game::value).sum();
    }

    public enum Play {ROCK, PAPER, SCISSOR}

    public enum State {WIN, LOSS, DRAW}

    public record Game(Play elfPlay, Play myPlay, State state) {
        public static final Map<Play, Map<Play, State>> STATE_COMPUTER = Map.of(
                ROCK, Map.of(ROCK, DRAW, PAPER, WIN, SCISSOR, LOSS),
                PAPER, Map.of(ROCK, LOSS, PAPER, DRAW, SCISSOR, WIN),
                SCISSOR, Map.of(ROCK, WIN, PAPER, LOSS, SCISSOR, DRAW));
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
