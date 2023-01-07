package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;
import static pro.verron.aoc.y22.Day02.Play.*;
import static pro.verron.aoc.y22.Day02.State.*;

public record Day02(List<Strategy> strategies) {

    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 2);
        Day02 sample = new Day02(aoc.testStream().map(Day02::parseStrategy).toList());
        Day02 custom = new Day02(aoc.trueStream().map(Day02::parseStrategy).toList());
        assertEquals(sample.rockPaperScissors(Day02::XYZareMoves), 15);
        assertEquals(custom.rockPaperScissors(Day02::XYZareMoves), 10404);
        assertEquals(sample.rockPaperScissors(Day02::XYZareIntents), 12);
        assertEquals(custom.rockPaperScissors(Day02::XYZareIntents), 10334);
    }

    static Strategy parseStrategy(String line) {
        return parseStrategy(line.split(" "));
    }

    static Strategy parseStrategy(String[] tokens) {
        return new Strategy(tokens[0].charAt(0), tokens[1].charAt(0));
    }

    record Strategy(Character elfToken, Character myToken){ }

    private static Game XYZareMoves(Strategy strategy) {
        Function<Character, Play> elfMoveParser = parsePlay('A', 'B', 'C');
        Function<Character, Play> yourMoveParser = parsePlay('X', 'Y', 'Z');
        return new Game(elfMoveParser.apply(strategy.elfToken()), yourMoveParser.apply(strategy.myToken()));
    }

    private static Game XYZareIntents(Strategy strategy) {
        Function<Character, Play> elfMoveParser = parsePlay('A', 'B', 'C');
        Function<Character, State> intentParser = parseIntent('X', 'Y', 'Z');
        return new Game(elfMoveParser.apply(strategy.elfToken()), intentParser.apply(strategy.myToken()));
    }

    private static Function<Character, Play> parsePlay(Character rock, Character paper, Character scissor) {
        return token -> {
            if (token.equals(rock)) return ROCK;
            else if (token.equals(paper)) return PAPER;
            else if (token.equals(scissor)) return SCISSOR;
            throw new IllegalStateException("Unexpected value: " + token);
        };
    }

    private static Function<Character, State> parseIntent(Character loss, Character draw, Character win) {
        return token -> {
            if (token.equals(loss)) return LOSS;
            else if (token.equals(draw)) return DRAW;
            else if (token.equals(win)) return WIN;
            throw new IllegalStateException("Unexpected value: " + token);
        };
    }

    private int rockPaperScissors(Function<Strategy, Game> player) {
        return this.strategies.stream().map(player).mapToInt(Game::value).sum();
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
