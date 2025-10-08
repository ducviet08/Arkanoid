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
        paddle = new Paddle(350, 550, 100, 20, 5);
        ball = new Ball(395, 530, 10, 10, 2, 1, -1);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        gameState = GameState.START;
        lastPowerUpTime = 0;
        activePowerUp = null;
        for (int i = 0; i < 5; i++) {
            bricks.add(new NormalBrick(100 + i * 80, 50, 70, 20));
            bricks.add(new StrongBrick(100 + i * 80, 80, 70, 20));
        }
    }

    public void startGame() {
        System.out.println("Game Started!");
        gameState = GameState.PLAYING;
    }

    public void updateGame() {
        if (gameState != GameState.PLAYING) {
            return;
        }
        paddle.update();
        ball.update();

        if (activePowerUp != null) {
            if (System.currentTimeMillis() - lastPowerUpTime > activePowerUp.getDuration()) {
                activePowerUp.removeEffect(paddle);
                activePowerUp = null;
            }
        }
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update();
            if (pu.getY() > 600) {
                powerUpIterator.remove();
            } else if (paddle.checkCollision(pu)) {
                paddle.applyPowerUp(pu);
                if (pu instanceof FastBallPowerUp) {
                    ((FastBallPowerUp) pu).setGameBall(ball);
                }
                activePowerUp = pu;
                lastPowerUpTime = System.currentTimeMillis();
                powerUpIterator.remove();
            }
        }
        checkCollisions();
        if (lives == 0) {
            gameOver();
        }
        if (bricks.isEmpty()) {
            levelComplete();
        }
    }

    /**
     * Xử lý đầu vào từ người chơi (sử dụng KeyCode.ordinal() từ JavaFX).
     */
    public void handleInput(int keyCodeOrdinal) {
        if (keyCodeOrdinal == KeyCode.LEFT.ordinal()) {
            paddle.moveLeft();
        } else if (keyCodeOrdinal == KeyCode.RIGHT.ordinal()) {
            paddle.moveRight();
        }
    }

    /**
     * Dừng di chuyển Paddle khi nhả phím (sử dụng KeyCode.ordinal() từ JavaFX).
     */
    public void handleKeyReleased(int keyCodeOrdinal) {
        if (keyCodeOrdinal == KeyCode.LEFT.ordinal() || keyCodeOrdinal == KeyCode.RIGHT.ordinal()) {
            paddle.stop();
        }
    }


    private void checkCollisions() {
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= 800) {
            ball.setDirectionX(-ball.getDirectionX());
        }
        if (ball.getY() <= 0) {
            ball.setDirectionY(-ball.getDirectionY());
        }
        if (ball.getY() + ball.getHeight() >= 600) {
            lives--;
            if (lives <= 0) {
                gameOver();
            }
            ball.setX(395);
            ball.setY(530);
            ball.setDirectionX(1);
            ball.setDirectionY(-1);
            paddle.setX(350);
        }

        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick);
                brick.takeHit();
                if (brick.isDestroyed()) {
                    score++;
                    brickIterator.remove();
                    if (Math.random() < 0.2) {
                        PowerUp newPowerup;
                        if (Math.random() < 0.5) {
                            newPowerup = new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20, 5000);
                        } else {
                            newPowerup = new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20, 5000, ball);
                        }
                        powerUps.add(newPowerup);
                    }
                }
            }

        }
    }

    public void gameOver() {
        gameState = GameState.GAME_OVER;
    }

    public void levelComplete() {
        gameState = GameState.LEVEL_COMPLETE;
    }

    public void renderAll() {
        paddle.render();
        ball.render();
        for (Brick brick : bricks) {
            brick.render();
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.render();
        }
        renderer.drawScoreAndLives(score, lives);
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