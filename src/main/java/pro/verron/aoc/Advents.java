package pro.verron.aoc;

import java.io.PrintStream;

public class Advents {
    public static void main(String[] args) {
        PrintStream writer = System.out;

        for (int i = 15; i <= 22; i++) {
            writer.printf("Year 20%02d:%n", i);
            December december = new December(i, writer);
            december.run();
            writer.println();
            writer.flush();
        }
    }

}
