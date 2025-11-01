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

import javafx.scene.input.KeyCode;

import static Arkanoid.Main.paddleImage;

public class GameManager {
    private Paddle paddle;
    private List<Ball> balls; // THAY ĐỔI: Từ Ball sang List<Ball>
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
    private long lastMixPowerUpTime;
    private PowerUp activeMixPowerUp;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    public enum GameState {
        START, PLAYING, PAUSED, GAME_OVER, LEVEL_COMPLETE
    }

    public enum GameMode {
        SINGLE_PLAYER,
        TWO_PLAYER
    }

    public GameManager(Renderer renderer) {
        this.renderer = renderer;
    }

    public void initializeGame() {
        paddle = new Paddle("/images/paddle2.png", 350, 550);

        // Khởi tạo danh sách bóng với 1 bóng ban đầu
        balls = new ArrayList<>();
        Ball mainBall = new Ball("/images/ball1.png", 395, 530, 1, -1);
        balls.add(mainBall);

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
        lastMixPowerUpTime = 0;
        activeMixPowerUp = null;
    }

    public void loadLevel(String filename) {
        bricks.clear();
        steels.clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/levels/" + filename));
            int numRows = lines.size();
            int numCols = lines.get(0).length();

            double brickWidth = 80;
            double brickHeight = 25;
            double gap = 5;

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
                        bricks.add(new StrongBrick("/images/brick10.png", x, y, brickWidth, brickHeight));
                    } else if (c == '3') {
                        bricks.add(new ExplosiveBrick("/images/brick8-1.png", x, y, brickWidth, brickHeight));
                    } else if (c == '4') {
                        bricks.add(new GlassBrick("/images/brick7.png", x, y, brickWidth, brickHeight));
                    } else if (c == '9') {
                        steels.add((new Steel("/images/steel.png", x, y, brickWidth, brickHeight)));
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

    public void startGame() {
        System.out.println("Game Started!");
        gameState = GameState.PLAYING;
    }

    public void updateGame() {
        if (gameState != GameState.PLAYING) {
            return;
        }

        paddle.update();

        // Update tất cả các bóng
        for (Ball ball : balls) {
            ball.updateRotation();
            ball.move(paddle, activeMixPowerUp);
        }

        // Xử lý Power-up hết hạn
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
        if (activeMixPowerUp != null) {
            if (System.currentTimeMillis() - lastMixPowerUpTime > activeMixPowerUp.getDuration()) {
                activeMixPowerUp.removeEffect(paddle);
                if (!(activeMixPowerUp instanceof StickyPaddlePowerUp st && st.isStuck())) {
                    activeMixPowerUp = null;
                }
            }
        }

        // Xử lý va chạm Power-up
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update();
            if (pu.getY() > 600) {
                powerUpIterator.remove();
            } else if (paddle.checkCollision(pu)) {
                SoundManager.playSound(SoundManager.SOUND_POWERUP_GET);
                paddle.applyPowerUp(pu);

                if (pu instanceof FastBallPowerUp) {
                    ((FastBallPowerUp) pu).setGameBall(getMainBall());
                    activeBallPowerUp = pu;
                    lastBallPowerUpTime = System.currentTimeMillis();

                } else if (pu instanceof ExtraLifePowerUp) {
                    if (lives < 5) {
                        this.lives++;
                    }

                } else if (pu instanceof FireBallPowerUp) {
                    ((FireBallPowerUp) pu).setGameBall(getMainBall());
                    activeBallPowerUp = pu;
                    lastBallPowerUpTime = System.currentTimeMillis();

                } else if (pu instanceof StickyPaddlePowerUp) {
                    ((StickyPaddlePowerUp) pu).setGameBall(getMainBall());
                    activeMixPowerUp = pu;
                    lastMixPowerUpTime = System.currentTimeMillis();

                } else if (pu instanceof MultipleBallPowerUp) {
                    ((MultipleBallPowerUp) pu).setBalls(balls);
                    pu.applyEffect(paddle);
                    // MultipleBall không cần tracking time vì hiệu ứng là tức thì

                } else {
                    activePaddlePowerUp = pu;
                    lastPaddlePowerUpTime = System.currentTimeMillis();
                }

                powerUpIterator.remove();
            }
        }

        checkCollisions();

        if (lives <= 0) {
            SoundManager.playSound(SoundManager.SOUND_LOSE_LIFE);
            gameOver();
        }
        if (bricks.isEmpty()) {
            levelComplete();
        }
    }

    public void handleInput(int keyCodeOrdinal) {
        Ball mainBall = getMainBall();
        if (mainBall != null && !mainBall.isActive() && keyCodeOrdinal == KeyCode.ENTER.ordinal()) {
            if(activeMixPowerUp instanceof StickyPaddlePowerUp stickyPaddle && paddle.isSticky() && stickyPaddle.isStuck()) {
                stickyPaddle.releaseBall(mainBall, paddle);
            } else {
                mainBall.setActive(true);
            }
        }
    }

    public void handleKeyReleased(int keyCodeOrdinal) {
        // Không cần dùng pressedKeys ở đây nữa
    }

    private void checkCollisions() {
        // Xóa các bóng rơi xuống đáy
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();

            if (ball.getY() + ball.getHeight() >= 600) {
                SoundManager.playSound(SoundManager.SOUND_LOSE_LIFE);

                // Nếu chỉ còn 1 bóng cuối cùng
                if (balls.size() == 1) {
                    ball.setActive(false);

                    if (activeBallPowerUp != null) {
                        activeBallPowerUp.removeEffect(paddle);
                        activeBallPowerUp = null;
                    }
                    if (activePaddlePowerUp != null) {
                        activePaddlePowerUp.removeEffect(paddle);
                        activePaddlePowerUp = null;
                    }
                    if (activeMixPowerUp != null) {
                        activeMixPowerUp.removeEffect(paddle);
                        activeMixPowerUp = null;
                    }

                    lives--;
                    if (lives <= 0) {
                        gameOver();
                        return;
                    }

                    ball.setOriginalSpeed();
                    ball.setX(395);
                    ball.setY(530);
                    ball.setDirectionX(1);
                    ball.setDirectionY(-1);
                    ball.resetBoost();
                    paddle.setX(350);
                } else {
                    // Xóa bóng này khỏi list
                    ballIterator.remove();
                    System.out.println("Ball removed! Remaining: " + balls.size());
                }
            }
        }

        // Kiểm tra va chạm với paddle
        for (Ball ball : balls) {
            if (ball.checkCollision(paddle)) {
                if (ball.isActive()) {
                    SoundManager.playSound(SoundManager.SOUND_PADDLE_HIT);
                }
                ball.bounceOff(paddle, activeMixPowerUp);
            }
        }

        // Kiểm tra va chạm với steel
        for (Steel steel : steels) {
            for (Ball ball : balls) {
                if (ball.checkCollision(steel)) {
                    SoundManager.playSound(SoundManager.SOUND_STEELBRICK_HIT);
                    ball.bounceOff(steel, activeMixPowerUp);
                }
            }
        }

        // Kiểm tra va chạm với brick
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();

            for (Ball ball : balls) {
                if (ball.checkCollision(brick)) {
                    if (activeBallPowerUp != null && activeBallPowerUp instanceof FireBallPowerUp) {
                        brick.takeDestroy();
                    } else {
                        SoundManager.playSound(SoundManager.SOUND_NORMALBRICK_HIT);
                        ball.bounceOff(brick, activeMixPowerUp);
                        brick.takeHit();
                    }

                    if (brick.isDestroyed()) {
                        if (brick instanceof ExplosiveBrick) {
                            ExplosiveBrick temp = (ExplosiveBrick) brick;
                            temp.explode(bricks, brick);
                        }

                        // Spawn power-up
                        if (Math.random() < 0.5) {
                            PowerUp newPowerup;
                            double rand = Math.random();
                            if (rand < 0.12) {
                                newPowerup = new ExpandPaddlePowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20, 10000);
                            } else if (rand < 0.24) {
                                newPowerup = new FastBallPowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20, 10000, getMainBall());
                            } else if (rand < 0.36) {
                                newPowerup = new ExtraLifePowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20);
                            } else if (rand < 0.48) {
                                newPowerup = new FireBallPowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20, 10000, getMainBall());
                            } else if (rand < 0.64) {
                                newPowerup = new ShrinkPaddlePowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20, 10000);
                            } else if (rand < 0.80) {
                                newPowerup = new StickyPaddlePowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20, 10000, getMainBall());
                            } else {
                                // 20% chance cho MultipleBall
                                newPowerup = new MultipleBallPowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20);
                            }
                            newPowerup = new MultipleBallPowerUp("/images/slow_ball.png", brick.getX(), brick.getY(), 20, 20);
                            powerUps.add(newPowerup);
                        }

                        score++;
                        brickIterator.remove();
                        break; // Dừng kiểm tra các bóng khác với brick này
                    }
                }
            }
        }
    }

    public void gameOver() {
        SoundManager.playSound(SoundManager.SOUND_GAME_OVER);
        gameState = GameState.GAME_OVER;
    }

    public void levelComplete() {
        SoundManager.playSound(SoundManager.SOUND_LEVEL_COMPLETE);
        gameState = GameState.LEVEL_COMPLETE;
    }

    // -----------------------------------------------------------------
    // --- GETTERS VÀ SETTERS ---
    // -----------------------------------------------------------------

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

    /**
     * Lấy bóng chính (bóng đầu tiên trong list)
     */
    public Ball getMainBall() {
        return balls.isEmpty() ? null : balls.get(0);
    }

    /**
     * Lấy tất cả các bóng
     */
    public List<Ball> getBalls() {
        return balls;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
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

    public long getLastMixPowerUpTime() {
        return lastMixPowerUpTime;
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

    public void setMixLastPowerTime(long ballMixPowerTime) {
        this.lastMixPowerUpTime = ballMixPowerTime;
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

    public PowerUp getMixActivePowerUp() {
        return activeMixPowerUp;
    }

    public void setMixActivePowerUp(PowerUp activeMixPowerUp) {
        this.activeMixPowerUp = activeMixPowerUp;
    }

    public void setActivePowerUpByName(String className) {
        if (className == null || className.equals("null")) {
            return;
        }

        System.out.println("Loading active power-up: " + className);
        Ball mainBall = getMainBall();

        if (className.equals("ExpandPaddlePowerUp")) {
            this.activePaddlePowerUp = new ExpandPaddlePowerUp(paddleImage, 0, 0, 0, 0, 5000);
            activePaddlePowerUp.applyEffect(paddle);

        } else if (className.equals("FastBallPowerUp")) {
            this.activeBallPowerUp = new FastBallPowerUp("/images/fast_ball.png", 0, 0, 0, 0, 5000, mainBall);
            activeBallPowerUp.applyEffect(paddle);

        } else if (className.equals("FireBallPowerUp")) {
            this.activeBallPowerUp = new FireBallPowerUp("/images/fire_ball.png", 0, 0, 0, 0, 5000, mainBall);
            activeBallPowerUp.applyEffect(paddle);

        } else if (className.equals("ShrinkPaddlePowerUp")) {
            this.activePaddlePowerUp = new ShrinkPaddlePowerUp(paddleImage, 0, 0, 0, 0, 5000);
            activePaddlePowerUp.applyEffect(paddle);

        } else if (className.equals("StickyPaddlePowerUp")) {
            this.activeMixPowerUp = new StickyPaddlePowerUp("/images/paddle1.png", 0, 0, 0, 0, 10000, mainBall);
            activeMixPowerUp.applyEffect(paddle);
        }
    }
}