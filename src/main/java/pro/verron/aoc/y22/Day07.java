package pro.verron.aoc.y22;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class Day07 {
    private static int noSpaceLeftOnDevice2(Stream<String> listing) {

        ListingParser lp = new ListingParser();
        listing.forEach(lp::parse);
        ToIntFunction<Stream<Directory>> function = directories -> directories
                .filter(d -> d.size >= 30000000 - (70000000 - lp.size()))
                .min(comparing(Directory::size))
                .map(Directory::size)
                .orElseThrow();
        Stream<Directory> directories = lp.directories();
        return function.applyAsInt(directories);
    }

    public String ex1(Stream<String> content) {
        return String.valueOf(noSpaceLeftOnDevice(content));
    }

    private static int noSpaceLeftOnDevice(Stream<String> listing) {
        ToIntFunction<Stream<Directory>> function = directories2 -> directories2
                .mapToInt(Directory::size)
                .filter(size -> size < 100000)
                .sum();
        ListingParser lp = new ListingParser();
        listing.forEach(lp::parse);
        Stream<Directory> directories = lp.directories();
        return function.applyAsInt(directories);
    }

    public String ex2(Stream<String> content) {
        return String.valueOf(noSpaceLeftOnDevice2(content));
    }

    public static class File {
        private final int size;

        public File(int size) {
            this.size = size;
        }

        public File(String size) {
            this(Integer.parseInt(size));
        }
    }

    public static class Directory {
        private final Directory parent;
        private final String name;
        private final List<Directory> directories = new ArrayList<>();
        private int size = 0;

        public Directory(Directory parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public void add(Directory directory) {
            directories.add(directory);
        }

        public void add(File file) {
            resetSize(file.size);
        }

        private void resetSize(int size) {
            this.size += size;
            if (parent != null)
                parent.resetSize(size);
        }

        public Directory parent() {
            return parent;
        }

        public Stream<Directory> directories() {
            return directories.stream()
                    .mapMulti((dir, consumer) -> {
                        consumer.accept(dir);
                        dir.directories().forEach(consumer);
                    });
        }

        public int size() {
            return size;
        }

        public Directory get(String name) {
            return directories.stream().filter(d -> d.name.equals(name)).findFirst().orElseThrow();
        }
    }

    public static class ListingParser {
        public static final Pattern FILE_PATTERN = Pattern.compile("(\\d+) (.+)");
        public static final Pattern DIRECTORY_PATTERN = Pattern.compile("dir (.+)");
        private final Directory root = new Directory(null, "/");
        private Directory cwd = root;

        public void parse(String line) {
            Matcher fileMatcher = FILE_PATTERN.matcher(line);
            Matcher directoryMatcher = DIRECTORY_PATTERN.matcher(line);
            if (line.equals("$ cd /")) cwd = root;
            else if (line.equals("$ ls")) { /* DO NOTHING*/ } else if (directoryMatcher.matches())
                cwd.add(new Directory(cwd, directoryMatcher.group(1)));
            else if (fileMatcher.matches()) cwd.add(new File(fileMatcher.group(1)));
            else if (line.equals("$ cd ..")) cwd = cwd.parent();
            else if (line.startsWith("$ cd ")) cwd = cwd.get(line.substring(5));
        }

        public Stream<Directory> directories() {
            return Stream.concat(Stream.of(root), root.directories());
        }

        public int size() {
            return root.size();
        }
    }
}