package Arkanoid.controller;

import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String FILE_PATH = "Arkanoid/score.txt";
    private static final int MAX_SCORES = 10;
    private int minHighScore;
    private List<ScoreInput> highScores;

    public HighScoreManager() {
        minHighScore = 0;
        highScores = new ArrayList<>();
        loadHighScore();
    }

    public List<ScoreInput> getHighScores() {
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
                String[] parts = line.split("\\s+");
                int n = parts.length;
                int score = Integer.parseInt(parts[n - 1]);

                String name = "";
                for (int i = 0; i < n - 1; i++) {
                    if (i != n - 2) {
                        name += parts[i] + " ";
                    } else {
                        name += parts[i];
                    }
                }
                ScoreInput scoreInput = new ScoreInput(name, score);
                highScores.add(scoreInput);
            }
            if (!highScores.isEmpty()) {
                minHighScore = highScores.get(highScores.size() - 1).getPlayerScore();
            }
            updateHighScore();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("High score file not found.");
        } catch (Exception e) {
            System.out.println("Can't read the score.txt file!");
        }
    }

    public void saveHighScore() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (ScoreInput x : highScores) {
                writer.write(x.toString());
                writer.newLine();
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Can't read the score.txt file");
        }
    }

    public void updateHighScore() {
        highScores.sort((e1, e2) -> Integer.compare(e2.getPlayerScore(), e1.getPlayerScore()));
    }

    public boolean checkHighScore(int score) {
        if (score > minHighScore) {
            ScoreInput lowest = null;
            for (int i = 0; i < highScores.size(); i++) {
                if (highScores.get(i).getPlayerScore() < minHighScore) {
                    lowest = highScores.get(i);
                }
            }
            if (lowest != null) {
                highScores.remove(lowest);
                minHighScore = Collections.min(highScores, Comparator.comparingInt(ScoreInput::getPlayerScore)).getPlayerScore();
            } else {
                minHighScore = score;
            }
            return true;
        }
        return false;
    }

    public void addHighScores(String playerName, int score) {
        ScoreInput newScoreInput = new ScoreInput(playerName, score);
        highScores.add(newScoreInput);
    }

    public void deleteAllDataHighScore() {
        highScores.clear();
    }

    public int getMaxHighScore() {
        if (highScores.isEmpty()) {
            return 0;
        }
        return Collections.max(highScores, Comparator.comparingInt(ScoreInput::getPlayerScore)).getPlayerScore();
    }
}
