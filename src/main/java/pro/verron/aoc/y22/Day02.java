package pro.verron.aoc.y22;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static pro.verron.aoc.y22.Day02.Play.*;
import static pro.verron.aoc.y22.Day02.State.*;

public class Day02 {
    private static Game XYZAreMoves(Strategy strategy) {
        Function<Character, Play> elfMoveParser = parsePlay('A', 'B', 'C');
        Function<Character, Play> yourMoveParser = parsePlay('X', 'Y', 'Z');
        return new Game(elfMoveParser.apply(strategy.elfToken()), yourMoveParser.apply(strategy.myToken()));
    }

    private static Game XYZAreIntents(Strategy strategy) {
        Function<Character, Play> elfMoveParser = parsePlay('A', 'B', 'C');
        Function<Character, State> intentParser = parseIntent('X', 'Y', 'Z');
        return new Game(elfMoveParser.apply(strategy.elfToken()), intentParser.apply(strategy.myToken()));
    }

    static Strategy parseStrategy(String line) {
        return parseStrategy(line.split(" "));
    }

    static Strategy parseStrategy(String[] tokens) {
        return new Strategy(tokens[0].charAt(0), tokens[1].charAt(0));
    }

    record Strategy(Character elfToken, Character myToken){ }

    public String ex1(Stream<String> content) {
        List<Strategy> strategies = content.map(Day02::parseStrategy).toList();
        int i = rockPaperScissors(strategies, Day02::XYZAreMoves);
        return String.valueOf(i);
    }

    public String ex2(Stream<String> content) {
        List<Strategy> strategies = content.map(Day02::parseStrategy).toList();
        int i = rockPaperScissors(strategies, Day02::XYZAreIntents);
        return String.valueOf(i);
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

    private int rockPaperScissors(List<Strategy> strategies, Function<Strategy, Game> player) {
        return strategies.stream().map(player).mapToInt(Game::value).sum();
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
