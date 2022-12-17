package pro.verron.aoc.y15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static java.lang.System.out;

public class Day4 {

    public static final HexFormat HEX_FORMAT = HexFormat.of();
    public static MessageDigest MD = null;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        MD = MessageDigest.getInstance("MD5");
        Path source = Path.of("input","y15", "day04-input.txt");
        out.println(ex1(Files.readString(source), "00000"));
        out.println(ex1(Files.readString(source), "000000"));
    }

    private static String ex1(String content, final String prefix) {
        int i = -1;
        String hex = "";
        while (!hex.startsWith(prefix)) {
            MD.reset();
            i = i + 1;
            String key = content + i;
            byte[] keyBytes = key.getBytes();
            byte[] digest = MD.digest(keyBytes);
            hex = HEX_FORMAT.formatHex(digest);
        }
        return String.valueOf(i);
    }
}
