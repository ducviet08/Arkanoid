// Arkanoid/model/FastBallPowerUp.java
package model;

public class FastBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 1.5; // Tăng tốc bóng lên 1.5 lần
    private Ball gameBall; // Tham chiếu đến quả bóng chính của game

    public FastBallPowerUp(double x, double y, double width, double height, double speed, long duration) {
        super(x, y, width, height, FALL_SPEED, duration);
    }

    // Setter để GameManager có thể thiết lập tham chiếu đến ball
    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getOriginalSpeed() * SPEED_MULTIPLIER);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getOriginalSpeed());
    }
}