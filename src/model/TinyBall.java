// Arkanoid/model/FastBallPowerUp.java
package model;

public class TinyBall extends PowerUp {
    private static final double SPEED_MULTIPLIER = 2.0; // tăng tốc đô cho bóng
    private static final double SIZE_MULTIPLIER = 0.7; // giảm kích thước bóng tốc bóng
    private Ball ball; // Tham chiếu đến quả bóng chính của game

    public TinyBall(String pathImage,double x, double y, double width, double height, long duration, Ball ball) {
        super(pathImage, x, y, width, height, FALL_SPEED, duration);
        this.ball = ball;
    }

    // Setter để GameManager có thể thiết lập tham chiếu đến ball
    public void setGameBall(Ball gameBall) {
        this.ball = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        ball.setHeight(Ball.ORIGINAL_HEIGHT * SIZE_MULTIPLIER);
        ball.setWidth(Ball.ORIGINAL_WIDTH * SIZE_MULTIPLIER);
        ball.setSpeed(Ball.ORIGINAL_SPEED * SPEED_MULTIPLIER);
    }

    @Override
    public void removeEffect(Paddle paddle) {
        ball.setOriginalSpeed();
        ball.setOriginalHeight();
        ball.setOriginalWidth();
    }
}