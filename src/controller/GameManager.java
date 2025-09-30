// Arkanoid/controller/GameManager.java

package controller;

import model.*;
import view.Renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Import JavaFX KeyCode để so sánh (nếu bạn muốn)
import javafx.scene.input.KeyCode;


public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private int score;
    private int lives;
    private GameState gameState;
    private Renderer renderer;
    private long lastPowerUpTime;
    private PowerUp activePowerUp;

    public enum GameState {
        START, PLAYING, PAUSED, GAME_OVER, LEVEL_COMPLETE
    }

    public GameManager(Renderer renderer) {
        this.renderer = renderer;
        initializeGame();
    }

    private void initializeGame() {
    }

    public void startGame() {
        System.out.println("Game Started!");
        gameState = GameState.PLAYING;
    }

    public void updateGame() {
    }

    /**
     * Xử lý đầu vào từ người chơi (sử dụng KeyCode.ordinal() từ JavaFX).
     */
    public void handleInput(int keyCodeOrdinal) {
    }

    /**
     * Dừng di chuyển Paddle khi nhả phím (sử dụng KeyCode.ordinal() từ JavaFX).
     */
    public void handleKeyReleased(int keyCodeOrdinal) {
    }


    private void checkCollisions() {
    }

    public void gameOver() {
    }

    public void levelComplete() {
    }

    public void renderAll() {
    }
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }
}