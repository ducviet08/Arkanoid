// Arkanoid/model/ExpandPaddlePowerUp.java
package model;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final double EXPAND_AMOUNT = 50; // Lượng mở rộng thêm cho paddle
    private static final double BUFF_SPEED_OF_BALL = 1.25; // Tốc độ của bóng khi mở rộng paddle
    private static final double FALL_SPEED = 2; // Tốc độ rơi của power-up

    public ExpandPaddlePowerUp(double x, double y, double width, double height, double speed, long duration) {
        super(x, y, width, height, speed, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getSpeed() * BUFF_SPEED_OF_BALL);
        paddle.setWidth(paddle.getWidth() + EXPAND_AMOUNT);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getSpeed() / BUFF_SPEED_OF_BALL);
        paddle.setWidth(paddle.getWidth() - EXPAND_AMOUNT);
    }
}