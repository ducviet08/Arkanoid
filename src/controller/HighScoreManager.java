package Arkanoid.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String FILE_PATH = "src/data/highScore.txt";
    private static final int MAX_SCORES = 10;
    private int minHighScore;
    private List<Integer> highScores;

    public HighScoreManager() {
        minHighScore = 0;
        highScores = new ArrayList<>();
        loadHighScore();
    }

    public List<Integer> getHighScores() {
        return highScores;
    }

    public int getMinHighScore() {
        return minHighScore;
    }

    public void loadHighScore() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));

            String line;

            while ((line = reader.readLine()) != null && highScores.size() < MAX_SCORES) {
                int score = Integer.parseInt(line.trim());
                highScores.add(score);
            }
            if (!highScores.isEmpty()) {
                minHighScore = highScores.get(highScores.size() - 1);
            }
            updateHighScore();
            reader.close();
        } catch (FileNotFoundException e) {
            highScores.add(0);
            System.out.println("High score file not found.");
        } catch (Exception e) {
            System.out.println("Can't read the highScore.txt file!");
        }
    }

    public void saveHighScore() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (int x : highScores) {
                writer.write(Integer.toString(x));
                writer.newLine();
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Can't read the highScore.txt file!");
        }
    }

    public void updateHighScore() {
        Collections.sort(highScores, Collections.reverseOrder());
    }

    public void checkHighScore(int highScore) {
        if (highScore > minHighScore) {
            highScores.add(highScore);

            while (highScores.size() > 10) {
                Integer minValue = Collections.min(highScores);
                highScores.remove(minValue);
            }
            this.minHighScore = Collections.min(highScores);
            updateHighScore();
        }
    }

    public void deleteAllDataHighScore() {
        highScores.clear();
    }

    public int getMaxHighScore() {
        if (highScores.isEmpty()) {
            return 0;
        }
        return Collections.max(highScores);
    }
}
