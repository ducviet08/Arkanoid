//// Arkanoid/FastBallPowerUp.java
//
//
//public class FastBallPowerUp extends PowerUp {
//    private static final double SPEED_MULTIPLIER = 1.5; // Tăng tốc bóng lên 1.5 lần
//    private Ball gameBall; // Tham chiếu đến quả bóng chính của game
//
//    public static final double FAST_MIN_SPEED = 5;
//    public static final double FAST_MAX_SPEED = 8;
//    public static final double FAST_INITIAL_SPEED = 5.5;
//
//    public FastBallPowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
//        super(imagePath, x, y, width, height, FALL_SPEED, duration);
//        this.gameBall = ball;
//    }
//
//    // Setter để GameManager có thể thiết lập tham chiếu đến ball
//    public void setGameBall(Ball gameBall) {
//        this.gameBall = gameBall;
//    }
//
//    @Override
//    public void applyEffect(Paddle paddle) { // Paddle không thay đổi, chỉ ball thay đổi
//        System.out.println("FastBall PowerUp activated!");
//        gameBall.setImage("/images/fast_ball.png");
//        if (gameBall != null) {
//            gameBall.setMinSpeed(FAST_MIN_SPEED);
//            gameBall.setMaxSpeed(FAST_MAX_SPEED);
//            gameBall.setSpeed(FAST_INITIAL_SPEED);
//        }
//    }
//
//    @Override
//    public void removeEffect(Paddle paddle) { // Paddle không thay đổi, chỉ ball thay đổi
//        System.out.println("FastBall PowerUp deactivated!");
//        if (gameBall != null) {
//            gameBall.setImage(ballImage);
//            gameBall.resetSpeedLimits();
//            gameBall.clampSpeedToLimits();
//        }
//    }
//}


// Arkanoid/FastBallPowerUp.java
package Arkanoid.model.ball;

import Arkanoid.model.paddle.Paddle;
import Arkanoid.model.powerup.PowerUp;

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