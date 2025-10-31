// Arkanoid/model/TinyBall.java
package model;

public class TinyBall extends PowerUp {
    private static final double SPEED_MULTIPLIER = 1.8; // 1.8x tốc độ
    private static final double SIZE_MULTIPLIER = 0.6; // 0.6x kích thước
    private Ball ball;

    public TinyBall(String pathImage, double x, double y, double width, double height, long duration, Ball ball) {
        super(pathImage, x, y, width, height, FALL_SPEED, duration);
        this.ball = ball;
    }

    public void setGameBall(Ball gameBall) {
        this.ball = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        System.out.println("TinyBall PowerUp activated!");

        if (ball != null) {
            // Giảm kích thước
            ball.setRadius(Ball.ORIGINAL_RADIUS * SIZE_MULTIPLIER);

            // ✅ Set multiplier thay vì set speed trực tiếp
            ball.setBaseSpeedMultiplier(SPEED_MULTIPLIER);
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        System.out.println("TinyBall PowerUp deactivated!");

        if (ball != null) {
            // Reset kích thước
            ball.setRadius(Ball.ORIGINAL_RADIUS);

            // ✅ Reset multiplier về 1.0
            ball.setBaseSpeedMultiplier(1.0);
        }
    }
}