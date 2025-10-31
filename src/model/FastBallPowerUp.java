
// Arkanoid/model/FastBallPowerUp.java
package model;

import static Arkanoid.Main.ballImage;

public class FastBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 1.5; // 1.5x tốc độ
    private Ball gameBall;

    public FastBallPowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
        this.gameBall = ball;
    }

    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        System.out.println("FastBall PowerUp activated!");
        gameBall.setImage("/images/fast_ball.png");

        if (gameBall != null) {
            // ✅ Set multiplier thay vì set speed trực tiếp
            gameBall.setBaseSpeedMultiplier(SPEED_MULTIPLIER);
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        System.out.println("FastBall PowerUp deactivated!");

        if (gameBall != null) {
            // ✅ Reset về multiplier = 1.0
            gameBall.setBaseSpeedMultiplier(1.0);
            gameBall.setImage(ballImage);
        }
    }
}