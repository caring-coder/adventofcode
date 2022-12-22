package pro.verron.aoc.y22;

import org.paukov.combinatorics3.Generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class Day16 {
    public static void main(String[] args) throws IOException {
        Pattern pattern = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (\\w+(, \\w+)*)");
        List<Valve> valves = Files.lines(Path.of("input", "y22", "day16-input.txt"))
                .map(pattern::matcher).filter(Matcher::matches)
                .map(s -> new Valve(s.group(1), parseInt(s.group(2)), Arrays.asList(s.group(3).split(", "))))
                .toList();
        Map<String, Valve> valuableValves = valves.stream().filter(valve -> valve.rate() > 0 || valve.id().equals("AA")).collect(toMap(Valve::id, v -> v));
        Map<Route, Integer> costs = calculateCosts(valves);
        out.println(ex1(valuableValves, costs));
        out.println(ex2(valuableValves, costs));
    }

    private static Object ex1(Map<String, Valve> valuableValves1, Map<Route, Integer> costs1) {
        State initialState = new State(30, "AA", 0, new HashSet<>());
        return maximumPressureRelease(initialState, costs1, valuableValves1).currentPressure;
    }

    private static Object ex2(Map<String, Valve> valuableValves1, Map<Route, Integer> costs1) {
        var targets = new HashSet<>(valuableValves1.keySet());
        targets.remove("AA");
        return Generator.combination(targets).simple(targets.size() / 2).stream().mapToInt(valvesToVisit -> {
            var otherValvesToVisit = targets.stream().filter(not(valvesToVisit::contains)).collect(toSet());
            var myValves = maximumPressureRelease(new State(26, "AA", 0, new HashSet<>(valvesToVisit)), costs1, valuableValves1);
            var elefantValves = maximumPressureRelease(new State(26, "AA", 0, new HashSet<>(otherValvesToVisit)), costs1, valuableValves1);
            return myValves.currentPressure + elefantValves.currentPressure;
        }).max().orElse(0);
    }

    private static Map<Route, Integer> calculateCosts(List<Valve> valves) {
        Map<Route, Integer> costs = new HashMap<>();
        valves.forEach(source -> source.tunnels().forEach(target -> costs.put(new Route(source.id(), target), 1)));
        valves.forEach(via -> valves.forEach(from -> valves.stream().filter(to -> from != to && from != via && to != via).forEach(to -> {
            Route part1 = new Route(from.id(), via.id());
            Route part2 = new Route(via.id(), to.id());
            if (costs.containsKey(part1) && costs.containsKey(part2)) {
                Route direct = new Route(from.id(), to.id());
                var viaCosts = costs.get(part1) + costs.get(part2);
                costs.put(direct, min(viaCosts, costs.getOrDefault(direct, Integer.MAX_VALUE)));
            }
        })));

        var irrelevantValves = valves.stream().filter(valve -> valve.rate() <= 0 && !valve.id.equals("AA")).map(Valve::id).toList();
        var irrelevantRoutes = costs.keySet().stream().filter(route -> irrelevantValves.contains(route.from) || irrelevantValves.contains(route.to)).toList();
        irrelevantRoutes.forEach(costs::remove);
        return costs;
    }

    private static State maximumPressureRelease(State initialState, Map<Route, Integer> costs, Map<String, Valve> valves) {
        Queue<State> todo = new LinkedList<>(List.of(initialState));
        State max = null;

        do {
            var state = todo.remove();
            var newStates = costs.keySet().stream()
                    .filter(route -> route.from.equals(state.currentValveId))
                    .filter(route -> !state.visited.contains(route.to))
                    .filter(route -> costs.get(route) + 1 <= state.minutes)
                    .map(route -> state.moveToAndOpen(route.to(), costs.get(route), valves.get(route.to()).rate()))
                    .toList();

            if (newStates.size() == 0) {
                if (max == null || state.currentPressure > max.currentPressure) {
                    max = state;
                }
            }
            todo.addAll(newStates);
        } while (!todo.isEmpty());
        return max;
    }
    public record Route(String from, String to) {    }
    public record Valve(String id, int rate, List<String> tunnels) {    }
    private record State(int minutes, String currentValveId, int currentPressure, Set<String> visited) {
        State moveToAndOpen(String moveTo, int costs, int targetFlowrate) {
            Set<String> visited = new HashSet<>(this.visited) {{                add(currentValveId);            }};
            return new State(minutes - costs - 1, moveTo, currentPressure + targetFlowrate * (minutes - costs - 1), visited);
        }
    }
}
