package pro.verron.aoc.y15;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static java.util.stream.IntStream.iterate;

public class Day04 {
    private static final MessageDigest MD;

    static {
        try {
            MD = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error looking for MD5 algorithm", e);
        }
    }

    public String ex1(String content) {
        return ex(content, "00000");
    }

    public String ex2(String content) {
        return ex(content, "000000");
    }

    private String ex(String content, String prefix) {
        HexFormat HEX_FORMAT = HexFormat.of();
        long count = iterate(0, i -> i + 1)
                .mapToObj(i -> content + i)
                .map(String::getBytes)
                .map(MD::digest)
                .map(HEX_FORMAT::formatHex)
                .takeWhile(s -> !s.startsWith(prefix))
                .count();
        return String.valueOf(count);
    }
}
