package pro.verron.aoc;

import org.intellij.lang.annotations.PrintFormat;

public class AdventOfCodeException
        extends RuntimeException {
    public AdventOfCodeException(Exception exception) {
        super(exception);
    }

    public AdventOfCodeException(String msg) {
        super(msg);
    }

    public AdventOfCodeException(@PrintFormat String template, String... args) {
        super(template.formatted((Object[]) args));
    }
}
