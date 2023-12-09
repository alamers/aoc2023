package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.*;

public class Level8b {



    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level8-input.txt").toURI()));

        String[] lines = file.split("\n");
        String instructions = lines[0];

        Map<String,Node> nodes = readNodes(lines);

        List<Node> startNodes = nodes.keySet().stream().filter(n -> n.endsWith("A")).map(n->nodes.get(n)).toList();

        Map<Node,List<Long>> cycles = new HashMap<>();
        for( Node n : startNodes) {
            int i = 0;
            long count =0;
            Node startNode = n;
            List<Long> counts = new ArrayList<>();
            do  {
                char instruction = instructions.charAt(i);
                switch (instruction) {
                    case 'L' -> n = nodes.get(n.left);
                    case 'R' -> n = nodes.get(n.right);
                }
                i++;
                count++;
                if (i >= instructions.length()) {
                    i = 0;
                }
                if (n.end()) {
                    System.out.println(startNode + " -> " + n );
                    counts.add(count);
                }
            } while( !n.end() && n!=startNode);

            cycles.put(startNode, counts);
        }

        long l = 0;

        for (Node n : cycles.keySet()) {
            List<Long> counts = cycles.get(n);
            Long count = counts.get(0);

            if(l ==0) {
                l = count;
            } else {
                l = lcm(l, count);
            }
        }

        System.out.println(l);

    }


    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    private static boolean allEndWithZ(List<Node> activeNodes) {
        return activeNodes.stream().allMatch(n -> n.end());
    }

    private static Map<String,Node> readNodes(String[] lines) {
        Map<String,Node> nodes = new HashMap<>();
        for( int i=2; i<lines.length; i++) {
            String line = lines[i];
            String[] fields = line.replace("(","").replace(")","").split(",| ");
            String name = fields[0];
            String left = fields[2];
            String right = fields[4];
            nodes.put(name, new Node(name, left, right, name.endsWith("Z")));
        }
        return nodes;
    }

    record Node(String name, String left, String right, boolean end) {

    }


}
