package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level6 {

    record Race(long duration, long recordDistance) {

        long findPossibilities() {
            return findMax() - findMin() + 1;
        }

        long findMin() { // minimal button press to beat record
            for( long i=0; i<duration; i++) {
                long d = distance(i);
//                System.out.println("buttonpress of " + i + " gives distance " + d + ", record is " + recordDistance);
                if (d>recordDistance) {
//                    System.out.println("min:" + i);
                    return i;
                }
            }
            return 0;
        }

        long findMax() { // maximal button press to beat record
            for( long i=duration-1; i>=0; i--) {
                long d = distance(i);
                if (d>recordDistance) {
//                    System.out.println("max:" + i);
                    return i;
                }
            }
            return 0;
        }

        long distance(long button) {
            long runningTime = duration - button;
            long speed = button;
            long distance = speed * runningTime;

//            System.out.println("distance:" + distance);
            return distance;
        }

    }

    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level6-input.txt").toURI()));

        String[] lines = file.split("\n");
//        List<Race> races = readRaces(lines);

        List<Race> races = List.of(new Race(51699878L, 377117112241505L));
        races.forEach(r -> System.out.println(r));

        List<Long> possibilities = races.stream().map( r -> r.findPossibilities() ).toList();

        System.out.println("Case #" + 0 + ": " + possibilities.get(0));
        Long total = possibilities.get(0);
        for( int i=1;i<possibilities.size(); i++) {
            System.out.println("Case #" + i + ": " + possibilities.get(i));
            total *= possibilities.get(i);

        }

        System.out.println("Total:" + total);


    }

    private static List<Race> readRaces(String[] lines) {
        String[] line0 = lines[0].replaceAll("\\s+", ";").replace(":", "").split(";");
        String[] line1 = lines[1].replaceAll("\\s+", ";").replace(":", "").split(";");;

        System.out.println(lines[0].replaceAll("  ", " "));

        System.out.println(Arrays.toString(line0));
        System.out.println(Arrays.toString(line1));
        List<Race> races = new ArrayList<>();
        for( int i=1; i<line0.length; i++) {
            races.add (new Race( Long.valueOf(line0[i]), Long.valueOf(line1[i]) ));
        }

        return races;

    }

}
