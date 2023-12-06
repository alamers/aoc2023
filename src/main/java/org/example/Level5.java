package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Level5 {

    static int line = 0;
    private static List<SeedRange> seedsToPlant;
    private static List<Range> seedToSoil;
    private static List<Range> soilToFertilizer;
    private static List<Range> fertilizerToWater;
    private static List<Range> waterToLight;
    private static List<Range> lightToTemperature;
    private static List<Range> temperatureToHumidity;
    private static List<Range> humidityToLocation;

    private static boolean debug = false;

    public record SeedRange(long start, long length) {
    }

    public record Range(long destRangeStart, long sourceRangeStart, long length) {
    }

    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level5-input.txt").toURI()));

        String[] lines = file.split("\n");

        seedsToPlant = parseSeedsToPlant(lines);
        seedToSoil = parseMap(lines);
        soilToFertilizer = parseMap(lines);
        fertilizerToWater = parseMap(lines);
        waterToLight = parseMap(lines);
        lightToTemperature = parseMap(lines);
        temperatureToHumidity = parseMap(lines);
        humidityToLocation = parseMap(lines);

        Long lowest = null;
        for (int i = 0; i < seedsToPlant.size(); i++) {
            SeedRange sr = seedsToPlant.get(i);
            for (long s = sr.start; s < sr.start + sr.length; s++) {

                debug =  (s==82L);

                Long location = findLocation(s);
                if (lowest == null || location < lowest) {
                    lowest = location;
                }
//                System.out.println("Seed " + s + " is location " + location);
            }
        }

        System.out.println("Lowest location is " + lowest);
    }

    public static Long findLocation(Long seed) {
        Long soil = findMapping(seed, seedToSoil);
        Long fertilizer = findMapping(soil, soilToFertilizer);
        Long water = findMapping(fertilizer, fertilizerToWater);
        Long light = findMapping(water, waterToLight);
        Long temperature = findMapping(light, lightToTemperature);
        Long humidity = findMapping(temperature, temperatureToHumidity);
        Long location = findMapping(humidity, humidityToLocation);
        if (debug) {
            System.out.println("Seed " + seed + ": " + soil + " " + fertilizer + " " + water + " " + light + " " + temperature + " " + humidity + " " + location);
        }

        // Seed 82: 84 84 84 77 45 46 46
        // Seed 82: 84 84 84 84 52 53 53
        return location;
    }

    public static Long findMapping(Long source, List<Range> ranges) {
        if(debug) {
            System.out.println("Finding mapping for " + source + " in " + ranges);
        }
        for (Range range : ranges) {
            if (source >= range.sourceRangeStart && source < range.sourceRangeStart + range.length) {
                if(debug) System.out.println("  found mapping: " + source + " -> " + range.destRangeStart + " + " + (source - range.sourceRangeStart) );
                return range.destRangeStart + (source - range.sourceRangeStart);
            }
            if (source < range.sourceRangeStart) {
                if(debug) System.out.println("Breaking: " + source + " < " + range.sourceRangeStart );
                break; // should have been found by now
            }
        }
        return source;
    }

    private static List<SeedRange> parseSeedsToPlant(String[] lines) {
        List<SeedRange> seedsToPlant = new ArrayList<>();
        String[] fields = lines[0].split(" ");
        for (int i = 1; i < fields.length; i += 2) {
            long seed = Long.parseLong(fields[i]);
            long length = Long.parseLong(fields[i + 1]);
            seedsToPlant.add(new SeedRange(seed, length));
        }

        line++;
        line++;

        return seedsToPlant;
    }

    private static List<Range> parseMap(String[] lines) {
        List<Range> ranges = new ArrayList<>();
        line++;
        while (line < lines.length && !lines[line].isBlank()) {
            String[] fields = lines[line].split(" ");
            line++;

            long destRangeStart = Long.parseLong(fields[0]);
            long sourceRangeStart = Long.parseLong(fields[1]);
            long length = Long.parseLong(fields[2]);

            ranges.add(new Range(destRangeStart, sourceRangeStart, length));

        }
        line++;

        Collections.sort(ranges, Comparator.comparingLong(r -> r.sourceRangeStart));

//        ranges.forEach(System.out::println);
//        System.out.println("---");
        return ranges;
    }
}
