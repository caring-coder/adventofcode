package pro.verron;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

public class Day1 {
    public static void main(String[] args) throws IOException {
        Path source = Path.of("day1-input.txt");
        String content = Files.readString(source);
        out.println(totalCaloriesForNBiggestElfCarriers(content, 1));
        out.println(totalCaloriesForNBiggestElfCarriers(content, 3));
    }

    private static String totalCaloriesForNBiggestElfCarriers(String content, int nbElfs) {
        return stream(content.split("\n\n"))           // Split by bags
                .map(bag -> bag.split("\n"))           // Split bags items
                .map(Arrays::stream)                         // Make bags item streamable
                .map(bag -> bag.mapToInt(Integer::parseInt)) // Parse all item's calorie count
                .map(IntStream::sum)                         // Sum item's calorie count by bags
                .sorted(reverseOrder())                      // Sort all bags calorie sums from highest to lowest
                .limit(nbElfs)                               // Keep Nth first bags
                .reduce(Integer::sum)                        // Sum those bags calorie count
                .map(String::valueOf)                        // Convert to String
                .orElse("No elfs found");
    }
}
