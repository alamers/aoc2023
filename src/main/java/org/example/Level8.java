package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Level8 {



    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level8-input.txt").toURI()));

        String[] lines = file.split("\n");
        String instructions = lines[0];

        Map<String,Node> nodes = readNodes(lines);

        int i = 0;
        int count =0;
        Node node = nodes.get("AAA");
        while( !node.name.equals("ZZZ")) {
            System.out.println(node.name);
            char instruction = instructions.charAt(i);
            switch(instruction) {
                case 'L' -> node = nodes.get(node.left);
                case 'R' -> node = nodes.get(node.right);
            }
            i++;
            count++;
            if (i>=instructions.length()) {
                i = 0;
            }
        }
        System.out.println(count);

    }

    private static Map<String,Node> readNodes(String[] lines) {
        Map<String,Node> nodes = new HashMap<>();
        for( int i=2; i<lines.length; i++) {
            String line = lines[i];
            String[] fields = line.replace("(","").replace(")","").split(",| ");
            String name = fields[0];
            String left = fields[2];
            String right = fields[4];
            nodes.put(name, new Node(name, left, right));
        }
        return nodes;
    }

    record Node(String name, String left, String right) {

    }


}
