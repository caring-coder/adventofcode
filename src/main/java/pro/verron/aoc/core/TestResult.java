package pro.verron.aoc.core;

public record TestResult(Test test, boolean success, String actual, String expected) {
}
