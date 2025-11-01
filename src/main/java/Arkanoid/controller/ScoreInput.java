package Arkanoid.controller;

public class ScoreInput {
    private String playerName;
    private int playerScore;

    public ScoreInput() {
        this.playerName = "null";
        this.playerScore = 0;
    }

    public ScoreInput(String namePlayer, int score) {
        this.playerName = namePlayer;
        this.playerScore = score;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public String toString() {
        return playerName + " " + Integer.toString(playerScore);
    }
}
