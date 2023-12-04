package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Level3 {
    private static int maxx;
    private static int maxy;

    record Number(int sx, int ex, int y, int value) {
    }

    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3.class.getResource("/level3-input.txt").toURI()));

        List<List<Integer>> matrix = new ArrayList<>();

        String[] lines = file.split("\n");
        maxy = lines.length;
        for (String line : lines) {
            List<Integer> row = new ArrayList<>();
            char[] chars = line.toCharArray();
            for (char c : chars) {
                row.add(isSymbol(c) ? 1 : 0);
            }
            maxx = row.size();
            matrix.add(row);
        }

        int sum = 0;
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {

                String number = "";
                int startx = x;
                while (x<maxx && isNumber(line.charAt(x))) {
                    number += line.charAt(x);
                    x++;
                }

                int endx = x - 1;

                if (!number.isBlank()) {
//                    System.out.println("found nr: " + number);
                    int value = Integer.parseInt(number);

                    if (isAdjacent(matrix, startx, endx, y)) {
//                        System.out.println("adjacent!");
                        sum += value;
                    } else {
                        System.out.println("not adjacent: " + number + " at " + startx + "," + y + " - " + endx + "," + y );
                    }
                }

            }
        }
        System.out.println(sum);

    }

    private static boolean isAdjacent(List<List<Integer>> matrix, int startx, int endx, int y) {

        // check column left
        if (startx > 0) {
            if (get(matrix, startx - 1, y)) {
                return true;
            }
            if (y > 0 && get(matrix, startx - 1, y - 1)) {
                return true;
            }
            if (y <maxy-1 && get(matrix, startx - 1, y + 1)) {
                return true;
            }
        }

        if (endx < maxx-1) {
            if (get(matrix, endx + 1, y)) {
                return true;
            }
            if (y > 0 && get(matrix, endx + 1, y - 1)) {
                return true;
            }
            if (y <maxy-1 && get(matrix, endx + 1, y + 1)) {
                return true;
            }
        }

        // check row above
        if (y > 0) {
            for (int x = startx; x <= endx; x++) {
                if (get(matrix, x, y - 1)) {
                    return true;
                }
            }
        }
        if (y <maxy-1) {
            for (int x = startx; x <= endx; x++) {
                if (get(matrix, x, y + 1)) {
                    return true;
                }
            }
        }

        return false;

    }

    private static boolean get(List<List<Integer>> matrix, int x, int y) {
        return matrix.get(y).get(x) == 1;
    }

    private static boolean isNumber(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        } else {
            return false;
        }

    }

    private static boolean isSymbol(char c) {
        if (c == '.' || (c >= '0' && c <= '9')) {
            return false;
        } else {
            return true;
        }

    }
}
