// Arkanoid/model/FastBallPowerUp.java
package model;

public class FastBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 2; // Tăng tốc bóng lên 2 lần
    private Ball ball; // Tham chiếu đến quả bóng chính của game

    public FastBallPowerUp(double x, double y, double width, double height, long duration, Ball ball) {
        super(x, y, width, height, FALL_SPEED, duration);
        this.ball = ball;
    }

    // Setter để GameManager có thể thiết lập tham chiếu đến ball
    public void setGameBall(Ball gameBall) {
        this.ball = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        if (ball != null) {
            ball.setSpeed(ball.getOriginalSpeed() * SPEED_MULTIPLIER);
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        if (ball != null) {
            ball.setSpeed(ball.getOriginalSpeed());
        }
    }
}