// Arkanoid/model/ExpandPaddlePowerUp.java
package model;

public class ShrinkPaddle extends PowerUp {
    private static final double SHRINK_AMOUNT = 50; // Lượng mở rộng thêm cho paddle
    private static final double BUFF_SPEED_OF_BALL = 0.75; // Tốc độ của bóng khi mở rộng paddle

    public ShrinkPaddle(double x, double y, double width, double height, long duration) {
        super(x, y, width, height, FALL_SPEED, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getOriginalSpeed() * BUFF_SPEED_OF_BALL);
        paddle.setWidth(paddle.getOriginalWidth() - SHRINK_AMOUNT);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getOriginalSpeed());
        paddle.setWidth(paddle.getOriginalWidth());
    }
}