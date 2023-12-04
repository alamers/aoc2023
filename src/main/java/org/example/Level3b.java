package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Level3b {
    private static int maxx;
    private static int maxy;

    record Number(int sx, int ex, int y, int value) {
    }

    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level3-input.txt").toURI()));

        List<List<Integer>> matrix = new ArrayList<>();
        Map<Integer,Integer> numbers = new HashMap<>();

        int numberCount = 0;
        String[] lines = file.split("\n");
        maxy = lines.length;
        for (String line : lines) {
            List<Integer> row = new ArrayList<>();
            char[] chars = line.toCharArray();
            for (char c : chars) {
                row.add(isGear(c) ? -1 : 0);
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
                    numberCount++;
                    System.out.println("Found number " + number + " with count " + numberCount);
                    numbers.put(numberCount,Integer.parseInt(number));
                    for (int i = startx; i <= endx; i++) {
                        System.out.println("Setting (" + i +"," + y +") to " + numberCount);
                        matrix.get(y).set(i, numberCount);
                    }
                }
            }
        }

//        for (int y = 0; y < maxy; y++) {
//            System.out.println();
//            for (int x=0; x<maxx; x++) {
//                System.out.print(matrix.get(y).get(x) + " ");
//            }
//        }
//        System.exit(1);

        for( int x=0; x<maxx; x++) {
            for( int y=0; y<maxy; y++) {
                if (matrix.get(y).get(x) == -1) {
                    Set<Integer> an = isAdjacent(matrix, x, y);
                    if (an.size()==2) {
                        Iterator<Integer> it = an.iterator();
                        int v = it.next();
                        int w = it.next();
                        sum += numbers.get(v)*numbers.get(w);
                    }
                }
            }
            System.out.println();
        }

        System.out.println(sum);

    }

    private static Set<Integer> isAdjacent(List<List<Integer>> matrix, int x, int y) {

        System.out.println("checking gear at " + x + "," + y + " (= " + get(matrix, x, y) + ")");

        Set<Integer> numbers = new HashSet<>();

        // check column left
        if (x > 0) {
            if (get(matrix, x - 1, y)>0) {
                numbers.add(get(matrix, x - 1, y));
            }
            if (y > 0 && get(matrix, x - 1, y - 1)>0) {
                numbers.add( get(matrix, x - 1, y - 1));
            }
            if (y <maxy-1 && get(matrix, x - 1, y + 1)>0) {
                numbers.add( get(matrix, x - 1, y + 1));
            }
        }

        if (x < maxx-1) {
            if (get(matrix, x + 1, y)>0) {
                numbers.add( get(matrix, x + 1, y));
            }
            if (y > 0 && get(matrix, x + 1, y - 1)>0) {
                numbers.add( get(matrix, x + 1, y - 1));
            }
            if (y <maxy-1 && get(matrix, x + 1, y + 1)>0) {
                numbers.add( get(matrix, x + 1, y + 1));
            }
        }

        // check row above
        if (y > 0) {
            if (get(matrix, x, y - 1)>0) {
                numbers.add( get(matrix, x, y - 1));
            }
        }
        if (y <maxy-1) {
            if (get(matrix, x, y + 1)>0) {
                numbers.add( get(matrix, x, y + 1));
            }
        }

        System.out.println("Found: " + numbers);

        return numbers;

    }

    private static int get(List<List<Integer>> matrix, int x, int y) {
        return matrix.get(y).get(x);
    }

    private static boolean isNumber(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        } else {
            return false;
        }

    }

    private static boolean isGear(char c) {
        if (c == '*') {
            return true;
        } else {
            return false;
        }

    }
}
