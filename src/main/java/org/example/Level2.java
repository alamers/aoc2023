package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level2 {



    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level2-input.txt").toURI()));


        long sum = 0L;
        String[] lines = file.split("\n");
        for (String line: lines) {
            String[] fields = line.split(":");
            long gameNr = Long.valueOf(fields[0].split(" ")[1]);
            String[] sets = fields[1].split(";");
            System.out.println("Game " + gameNr);
            Map<String, Long> maxs = new HashMap<>();
            maxs.put("red", -1L);
            maxs.put("green", -1L);
            maxs.put("blue", -1L);

            for (String set: sets) {
                System.out.print(" set: ");
                String[] cubes = set.split(",");
                for( String cube : cubes) {
                    cube = cube.trim();
                    String[] cubeFields = cube.split(" ");
                    long count = Long.valueOf(cubeFields[0]);
                    String color = cubeFields[1];
                    System.out.print("" + count + " " + color +",");

                    if (maxs.get(color)<count) {
                        maxs.put(color,count);
                    }
                }

            }

            long power = maxs.get("red") * maxs.get("green") * maxs.get("blue");
            System.out.println("game " + gameNr + ": power " + power);
            sum += power;

        }

        System.out.println(sum);
    }

    record Game(long game, List<GameSet> sets) {
    }

    record GameSet(Map<String, Long> cubes) {

    }

}
