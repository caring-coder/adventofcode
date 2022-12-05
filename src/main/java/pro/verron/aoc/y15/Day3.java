package pro.verron.aoc.y15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.out;
import static java.util.Arrays.sort;

public class Day3 {
    public static void main(String[] args) throws IOException {
        Path source = Path.of("input","y15", "day3-input.txt");
        out.println(ex1(Files.readString(source)));
        out.println(ex2(Files.readString(source)));
    }

    private static String ex1(String content) {
        Position position = new Position(0, 0);
        Set<Position> positions = new HashSet<>();
        positions.add(position);
        for(Character c : content.toCharArray()){
            position = switch (c){
                case '>' -> position.east();
                case '^' -> position.north();
                case '<' -> position.west();
                case 'v' -> position.south();
                default -> throw new RuntimeException("Unexpected token " + c);
            };
            positions.add(position);
        }
        return String.valueOf(positions.size());
    }

    private static String ex2(String content) {
        Position santaPosition = new Position(0, 0);
        Position roboSantaPosition = new Position(0, 0);
        boolean santaTurn = true;
        Set<Position> positions = new HashSet<>();
        positions.add(santaPosition);
        positions.add(roboSantaPosition);
        for(Character c : content.toCharArray()){
            if(santaTurn) santaPosition = switch (c){
                case '>' -> santaPosition.east();
                case '^' -> santaPosition.north();
                case '<' -> santaPosition.west();
                case 'v' -> santaPosition.south();
                default -> throw new RuntimeException("Unexpected token " + c);
            };
            else roboSantaPosition = switch (c){
                case '>' -> roboSantaPosition.east();
                case '^' -> roboSantaPosition.north();
                case '<' -> roboSantaPosition.west();
                case 'v' -> roboSantaPosition.south();
                default -> throw new RuntimeException("Unexpected token " + c);
            };
            positions.add(santaPosition);
            positions.add(roboSantaPosition);
            santaTurn = !santaTurn;
        }
        return String.valueOf(positions.size());
    }


    private record Box(int length, int depth, int height) {
        Box(int length, int depth, int height){
            int[] dimensions = {length, depth, height};
            sort(dimensions);
            this.length = dimensions[2];
            this.height = dimensions[1];
            this.depth = dimensions[0];
        }
        public int surface() {
                return 2 * height * length
                        + 2 * height * depth
                        + 2 * length * depth;
        }

        public int smallestSizeArea() {
            return depth * height;
        }

        public int smallestSizePerimeter() {
            return 2 * depth
                    + 2 * height;
        }

        public int volume() {
            return depth * height * length;
        }
    }

    private record Position(int x, int y) {
        public Position east() {
            return new Position(x, y + 1);
        }

        public Position north() {
            return new Position(x + 1, y);
        }

        public Position west() {
            return new Position(x, y - 1);
        }

        public Position south() {
            return new Position(x - 1, y);
        }
    }
}
