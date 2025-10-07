// Arkanoid/model/FastBallPowerUp.java
package model;

public class SlowBall extends PowerUp {
    private static final double SPEED_MULTIPLIER = 0.5; // Tăng tốc bóng lên 1.5 lần
    private Ball ball; // Tham chiếu đến quả bóng chính của game

    public SlowBall(double x, double y, double width, double height, long duration, Ball ball) {
        super(x, y, width, height, FALL_SPEED, duration);
        this.ball = ball;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        ball.setSpeed(ball.getOriginalSpeed() * SPEED_MULTIPLIER);
    }

    @Override
    public void removeEffect(Paddle paddle) {
        ball.setSpeed(ball.getOriginalSpeed());
    }

    // Setter để GameManager có thể thiết lập tham chiếu đến ball
    public void setGameBall(Ball gameBall) {
        this.ball = gameBall;
    }

}