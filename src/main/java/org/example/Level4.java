package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Level4 {
    static final int N_WINNING_NRS = 10;

    record Line (Integer cardNr, List<Integer> winningNrs, List<Integer> numbers) {

        List<Integer> winningNumbers() {
            List<Integer> n = new ArrayList<>(numbers);
            n.retainAll(winningNrs);
            return n;
        }

        long score() {
            System.out.println("CardNr " + cardNr + " has " + winningNumbers().size() + " winning numbers: score " + Math.pow(2, winningNumbers().size()-1) + " points");
            return (long) Math.pow(2, winningNumbers().size()-1);
        }

    }

    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level4-input.txt").toURI()));


        String[] lines = file.split("\n");
        List<Line> lineList = Arrays.stream(lines).map(l->ToLine(l)).toList();

        Map<Integer, Long> cardCounts = new HashMap<>();
        for( int i=0; i<lineList.size(); i++) {
            cardCounts.put( i, 1L);
        }

        // stream lineList to summarize the count of each line
//        long total = 0;
//        for( Line l : lineList) {
//            System.out.println("Card: " + l);
//            total += l.score();
//        }
//        System.out.println(total);

        for( int i=0; i<lineList.size(); i++) {
            Line l = lineList.get(i);
            int count = l.winningNumbers().size();
            System.out.println("Card: " + l + " has " + count + " winning numbers");
            long nrOfCopies = cardCounts.get(i);
            // add copies
            for( int j=i+1; j<i+1+count; j++) {
                cardCounts.put(j, cardCounts.get(j)+nrOfCopies);
                System.out.println("  adding copies for card " + j + " to " + cardCounts.get(j));
            }
        }

        long sum =0;
        for(int i=0; i<lineList.size(); i++) {
            sum += cardCounts.get(i);
        }

        System.out.println("Total sum: " + sum);
    }

    private static Line ToLine(String l) {
        String[] fields = l.replace("Card","").replace("|","").replace(":", "").replace("  ", " ").split(" +");
//        System.out.println("fields: " + Arrays.toString(fields));
        List<Integer> winningNrs = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for( int i=2; i<fields.length; i++) {
            if( i<2+N_WINNING_NRS) {
                winningNrs.add(Integer.parseInt(fields[i]));
            } else {
                numbers.add(Integer.parseInt(fields[i]));
            }
        }

        return new Line(Integer.parseInt(fields[1]), winningNrs, numbers);

    }
}
