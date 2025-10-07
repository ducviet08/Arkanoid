// Arkanoid/model/FastBallPowerUp.java
package model;

public class TinyBall extends PowerUp {
    private static final double SPEED_MULTIPLIER = 2.0; // tăng tốc đô cho bóng
    private static final double SIZE_MULTIPLIER = 0.7; // giảm kích thước bóng tốc bóng
    private Ball gameBall; // Tham chiếu đến quả bóng chính của game

    public TinyBall(double x, double y, double width, double height, long duration, Ball ball) {
        super(x, y, width, height, FALL_SPEED, duration);
    }

    // Setter để GameManager có thể thiết lập tham chiếu đến ball
    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.setHeight(ball.getOriginalHeight()*SIZE_MULTIPLIER);
        ball.setWidth(ball.getOriginalWidth()*SIZE_MULTIPLIER);
        ball.setSpeed(ball.getOriginalSpeed()*SPEED_MULTIPLIER);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.setHeight(ball.getOriginalHeight());
        ball.setWidth(ball.getOriginalWidth());
        ball.setSpeed(ball.getOriginalSpeed());
    }
}