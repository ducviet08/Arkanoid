// Arkanoid/controller/GameManager.java

package controller;

import model.*;
import view.Renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

// Import JavaFX KeyCode để so sánh (nếu bạn muốn)
import javafx.scene.input.KeyCode;


public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private List<Steel> steels;
    private int score;
    private int lives;
    private GameState gameState;
    private Renderer renderer;
    private long lastBallPowerUpTime;
    private long lastPaddlePowerUpTime;
    private PowerUp activeBallPowerUp;
    private PowerUp activePaddlePowerUp;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    public enum GameState {
        START, PLAYING, PAUSED, GAME_OVER, LEVEL_COMPLETE
    }

    public GameManager(Renderer renderer) {
        this.renderer = renderer;
    }

    public void initializeGame() {
        paddle = new Paddle("/images/paddle2.png",350, 550);
        ball = new Ball("/images/ball1.png",395, 530, 1, -1);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        steels = new ArrayList<>();
        score = 0;
        lives = 3;
        gameState = GameState.START;
        lastBallPowerUpTime = 0;
        lastPaddlePowerUpTime = 0;
        activeBallPowerUp = null;
        activePaddlePowerUp = null;

    }
    public void loadLevel(String filename) {
        bricks.clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/levels/" + filename));
            int numRows = lines.size();
            int numCols = lines.get(0).length();

            double brickWidth = 80;
            double brickHeight = 25;
            double gap = 5;

            // Căn giữa map theo chiều ngang
            double totalWidth = numCols * brickWidth + (numCols - 1) * gap;
            double marginLeft = (800 - totalWidth) / 2;
            double marginTop = 50;

            int rowIndex = 0;
            for (String line : lines) {
                int colIndex = 0;
                for (char c : line.toCharArray()) {
                    double x = marginLeft + colIndex * (brickWidth + gap);
                    double y = marginTop + rowIndex * (brickHeight + gap);

                    if (c == '1') {
                        bricks.add(new NormalBrick("/images/brick1.png", x, y, brickWidth, brickHeight));
                    } else if (c == '2') {
                        bricks.add(new StrongBrick("/images/brick6.png", x, y, brickWidth, brickHeight));
                    } else if (c == '3') {
                        bricks.add(new ExplosiveBrick("/images/brick2.png", x, y, brickWidth, brickHeight));
                    } else if (c == '4') {
                        bricks.add(new GlassBrick("/images/brick2.png", x, y, brickWidth, brickHeight));
                    } else if (c == '9') {
                        steels.add((new Steel("/images/brick2.png", x, y, brickWidth, brickHeight)));
                    }
                    colIndex++;
                }
                rowIndex++;
            }
            System.out.println("✅ Loaded " + filename);
        } catch (IOException e) {
            System.out.println("❌ Could not load level: " + filename);
        }
    }

//    private void initializeGame() {
//        paddle = new Paddle("/images/paddle2.png",350, 550, 100, 20, 5);
//        ball = new Ball("/images/normal_ball.png",395, 530, 15, 15, 2.5, 1, -1);
//        bricks = new ArrayList<>();
//        powerUps = new ArrayList<>();
//        steels = new ArrayList<>();
//        score = 0;
//        lives = 3;
//        gameState = GameState.START;
//        lastPowerUpTime = 0;
//        activePowerUp = null;
//        loadLevel("level1.txt");
//    }

    public void startGame() {
        System.out.println("Game Started!");
        gameState = GameState.PLAYING;
    }

    public void updateGame() {
        if (gameState != GameState.PLAYING) {
            return;
        }
        if (pressedKeys.contains(KeyCode.LEFT) && !pressedKeys.contains(KeyCode.RIGHT)) {
            paddle.moveLeft();
        } else if (pressedKeys.contains(KeyCode.RIGHT) && !pressedKeys.contains(KeyCode.LEFT)) {
            paddle.moveRight();
        } else {
            paddle.stop();
        }
        ball.updateRotation();
        paddle.update();
        ball.move(paddle);


        if (activeBallPowerUp != null) {
            if (System.currentTimeMillis() - lastBallPowerUpTime > activeBallPowerUp.getDuration()) {
                activeBallPowerUp.removeEffect(paddle);
                activeBallPowerUp = null;
            }
        }
        if (activePaddlePowerUp != null) {
            if (System.currentTimeMillis() - lastPaddlePowerUpTime > activePaddlePowerUp.getDuration()) {
                activePaddlePowerUp.removeEffect(paddle);
                activePaddlePowerUp = null;
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
                    activeBallPowerUp = pu;
                    lastBallPowerUpTime = System.currentTimeMillis();
                } else if (pu instanceof ExtraLifePowerUp) {
                    if (lives < 5) {
                        this.lives ++;
                    }
                } else if (pu instanceof FireBallPowerUp) {
                    ((FireBallPowerUp) pu).setGameBall(ball);
                    activeBallPowerUp = pu;
                    lastBallPowerUpTime = System.currentTimeMillis();
                } else {
                    activePaddlePowerUp = pu;
                    lastPaddlePowerUpTime = System.currentTimeMillis();
                }
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
        if (!ball.isActive() && keyCodeOrdinal == KeyCode.ENTER.ordinal()) {
            ball.setActive(true);
        }
//        if (keyCodeOrdinal == KeyCode.LEFT.ordinal()) {
//            paddle.moveLeft();
//        } else if (keyCodeOrdinal == KeyCode.RIGHT.ordinal()) {
//            paddle.moveRight();
//        }
        KeyCode key = KeyCode.values()[keyCodeOrdinal];
        pressedKeys.add(key);
    }

    /**
     * Dừng di chuyển Paddle khi nhả phím (sử dụng KeyCode.ordinal() từ JavaFX).
     */
    public void handleKeyReleased(int keyCodeOrdinal) {
//        if (keyCodeOrdinal == KeyCode.LEFT.ordinal() || keyCodeOrdinal == KeyCode.RIGHT.ordinal()) {
//            paddle.stop();
//        }
        KeyCode key = KeyCode.values()[keyCodeOrdinal];
        pressedKeys.remove(key);
    }


    private void checkCollisions() {
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= 800) {
            ball.setDirectionX(-ball.getDirectionX());
        }
        if (ball.getY() <= 0) {
            ball.setDirectionY(-ball.getDirectionY());
        }
        if (ball.getY() + ball.getHeight() >= 600) {
            ball.setActive(false);
            if(activeBallPowerUp != null) {
                activeBallPowerUp.removeEffect(paddle);
                activeBallPowerUp = null;
            }
            if(activePaddlePowerUp != null) {
                activePaddlePowerUp.removeEffect(paddle);
                activePaddlePowerUp = null;
            }
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

        Iterator<Steel> steelIterator = steels.iterator();
        while (steelIterator.hasNext()) {
            Steel steel = steelIterator.next();
            if (ball.checkCollision(steel)) {
                ball.bounceOff(steel);
            }
        }

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (ball.checkCollision(brick)) {
                if(activeBallPowerUp != null && activeBallPowerUp instanceof FireBallPowerUp) {
                    brick.takeDestroy();
                } else {
                    ball.bounceOff(brick);
                    brick.takeHit();
                }
                if (brick.isDestroyed()) {
                    score++;
                    // brickIterator.remove();
                    if (brick instanceof ExplosiveBrick) {
                        ExplosiveBrick temp = (ExplosiveBrick) brick;
                        temp.explode(bricks, brick);
                    }
                    if (Math.random() < 0.2) {
                        PowerUp newPowerup;
                        double rand = Math.random();
                        if (rand < 0.2) {
                            newPowerup = new ExpandPaddlePowerUp("/images/slow_ball.png",brick.getX(), brick.getY(), 20, 20, 5000);
                        } else if (rand < 0.4) {
                            newPowerup = new FastBallPowerUp("/images/slow_ball.png",brick.getX(), brick.getY(), 20, 20, 5000, ball);
                        } else if (rand < 0.6) {
                            newPowerup = new ExtraLifePowerUp("/images/slow_ball.png",brick.getX(), brick.getY(), 20, 20);
                        }  else if (rand < 0.8) {
                            newPowerup = new FireBallPowerUp("/images/slow_ball.png",brick.getX(), brick.getY(), 20, 20, 5000,ball);
                        } else {
                            newPowerup = new ShrinkPaddlePowerUp("/images/slow_ball.png",brick.getX(), brick.getY(), 20, 20, 5000);
                        }
                        powerUps.add(newPowerup);
                    }
                }
            }

        }

        for (int i = 0; i < bricks.size(); i++) {
            if (bricks.get(i).isDestroyed()) {
                score++;
                bricks.remove(i);
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
        for (Steel steel : steels) {
            steel.render();
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

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(List<PowerUp> powerUps) {
        this.powerUps = powerUps;
    }

    public List<Steel> getSteels() {
        return steels;
    }

    public void setSteels(List<Steel> steels) {
        this.steels = steels;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public PowerUp getBallActivePowerUp() {
        return activeBallPowerUp;
    }

    public PowerUp getPaddleActivePowerUp() {
        return activePaddlePowerUp;
    }

    public long getPaddleLastPowerUpTime() {
        return lastPaddlePowerUpTime;
    }

    public long getBallLastPowerUpTime() {
        return lastBallPowerUpTime;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setBallLastPowerUpTime(long lastBallPowerUpTime) {
        this.lastBallPowerUpTime = lastBallPowerUpTime;
    }

    public void setPaddleLastPowerUpTime(long lastPaddlePowerUpTime) {
        this.lastPaddlePowerUpTime = lastPaddlePowerUpTime;
    }

    public void setBallActivePowerUp(PowerUp activeBallPowerUp) {
        this.activeBallPowerUp = activeBallPowerUp;
    }

    public void setPaddleActivePowerUp(PowerUp activePaddlePowerUp) {
        this.activePaddlePowerUp = activePaddlePowerUp;
    }

    public void setActivePowerUpByName(String className) {
        if(className.equals("null")){
            return;
        }
        if (className.equals("ExpandPaddlePowerUp")) {
            this.activePaddlePowerUp = new ExpandPaddlePowerUp("/images/slow_ball.png",paddle.getX(), paddle.getY(), 0, 0, 5000); // Cần truyền tọa độ
            activePaddlePowerUp.applyEffect(paddle);
        } else if (className.equals("FastBallPowerUp")) {
            this.activeBallPowerUp = new FastBallPowerUp("/images/slow_ball.png",ball.getX(), ball.getY(), 0, 0, 5000, ball);
            activeBallPowerUp.applyEffect(paddle);
        } else if (className.equals("FireBallPowerUp")) {
            this.activeBallPowerUp = new FireBallPowerUp("/images/slow_ball.png",ball.getX(), ball.getY(), 0, 0, 5000, ball);
            activeBallPowerUp.applyEffect(paddle);
        } else if (className.equals("ShrinkPaddlePowerUp")) {
            this.activePaddlePowerUp = new ShrinkPaddlePowerUp("/images/slow_ball.png",paddle.getX(), paddle.getY(), 0, 0, 5000); // Cần truyền tọa độ
            activePaddlePowerUp.applyEffect(paddle);
        }
        // Thêm các power-up khác...
    }
}