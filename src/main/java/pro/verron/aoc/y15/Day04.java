package pro.verron.aoc.y15;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Day04 {
    public String ex1(String content) {
        return ex(content, "00000");
    }

    public String ex2(String content) {
        return ex(content, "000000");
    }

    private String ex(String content, final String prefix) {
        HexFormat HEX_FORMAT = HexFormat.of();
        MessageDigest MD;
        try {
            MD = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
