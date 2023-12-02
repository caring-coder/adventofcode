package pro.verron.aoc.core.test;

import java.nio.file.Path;

public record Test(Path in, Path out) {

    public Test(Path in) {
        this(in, in.resolveSibling(findOutPath(in.getFileName())));
    }

    static String findOutPath(Path fileName) {
        var inFilename = fileName
                .toString();
        return inFilename.replace(".in", ".out");
    }

    String dataset() {
        return in.getFileName()
                .toString()
                .split("\\.")[0];
    }
}
