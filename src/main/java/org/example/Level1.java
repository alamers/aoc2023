package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Level1 {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String file = Files.readString(Paths.get(Level1.class.getResource("/level1-input.txt").toURI()));

        String digit = "(one|two|three|four|five|six|seven|eight|nine|zero|[0-9])";

        Pattern p = Pattern.compile("^[a-z]*?" + digit + ".*" + digit + "[a-z]*?$");
        Pattern p2 = Pattern.compile("^[a-z]*?" + digit + "[a-z]*?$");

//        Matcher mm = p2.matcher("aonea");
//        mm.find();
//        System.out.println(mm.group(1));
//        System.out.println(mm.group(2));
//
//        System.exit(1);

        int sum = 0;
        String[] lines = file.split("\n");
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (m.matches()) {

                int x = parse(m.group(1));
                int y = parse(m.group(2));
                sum += x * 10;
                sum += y;
                System.out.println(line + " -> " + x * 10 + "," + y);
            } else {
                Matcher m2 = p2.matcher(line);
                if (m2.matches()) {
                    Integer x = parse(m2.group(1));
                    sum += x;
                    sum += x * 10;
                    System.out.println(line + " -> " + x + "x2");
                } else {
                    throw new RuntimeException("Invalid line: " + line);
                }
            }
        }

        System.out.println(sum);
    }

    private static int parse(String group) {
        return switch (group) {
            case "0", "zero" -> 0;
            case "1", "one" -> 1;
            case "2", "two" -> 2;
            case "3", "three" -> 3;
            case "4", "four" -> 4;
            case "5", "five" -> 5;
            case "6", "six" -> 6;
            case "7", "seven" -> 7;
            case "8", "eight" -> 8;
            case "9", "nine" -> 9;
            default -> throw new RuntimeException("Invalid digit: " + group);
        };
    }
}