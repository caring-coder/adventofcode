package pro.verron.aoc;

public record TestResult(int success, int total) {
    public boolean validate() {
        return success == total & total > 0;
    }
}
