package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Level9b {


    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level9-input.txt").toURI()));

        String[] lines = file.split("\n");

        List<List<Long>> histories = readHistories(lines);

        Long sum = 0L;
        for (List<Long> history : histories) {
            sum += extrapolate(history);
        }
        System.out.println(sum);
    }

    private static Long extrapolate(List<Long> history) {
        List<List<Long>> extrapolation = new ArrayList<>();
        extrapolation.add(history);

        List<Long> prediction = generatePrediction(history);
        while (notAllZero(prediction)) {
            System.out.println("nonzero prediction: " + prediction);
            extrapolation.add(prediction);
            prediction = generatePrediction(prediction);
        }
        extrapolation.add(prediction);

        System.out.println("extrapolation: " + extrapolation.size());

// from bottom up
        List<Long> e = null;
        for (int i = extrapolation.size() - 1; i >= 0; i--) {
            List<Long> f = extrapolation.get(i);
            if (e == null) {
                f.add(0, 0L);
            } else {
                f.add( 0,  f.get(0)- e.get(0));
            }
            System.out.println("  f = " + f);
            e = f;
        }

        List<Long> updatedH = extrapolation.get(0);
        return updatedH.get(0);
    }

    private static boolean notAllZero(List<Long> prediction) {
        for (Long l : prediction) {
            if (l != 0) {
                return true;
            }
        }
        return false;
    }

    private static List<Long> generatePrediction(List<Long> history) {
        List<Long> prediction = new ArrayList<>();
        for (int i = 0; i < history.size() - 1; i++) {
            Long x = history.get(i);
            Long y = history.get(i + 1);
            prediction.add(y - x);
        }
        return prediction;
    }


    private static List<List<Long>> readHistories(String[] lines) {
        List<List<Long>> histories = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(" ");
            List<Long> history = new ArrayList<>();
            for (String part : parts) {
                history.add(Long.parseLong(part));
            }
            histories.add(history);
        }
        return histories;

    }


}
