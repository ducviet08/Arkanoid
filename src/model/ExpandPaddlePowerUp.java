// Arkanoid/model/ExpandPaddlePowerUp.java
package model;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final double EXPAND_AMOUNT = 50; // Lượng mở rộng thêm cho paddle
    private static final double BUFF_SPEED_OF_BALL = 1.25; // Tốc độ của bóng khi mở rộng paddle

    public ExpandPaddlePowerUp(double x, double y, double width, double height, double speed, long duration) {
        super(x, y, width, height, FALL_SPEED, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getOriginalSpeed() * BUFF_SPEED_OF_BALL);
        paddle.setWidth(paddle.getOriginalWidth() + EXPAND_AMOUNT);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getOriginalSpeed());
        paddle.setWidth(paddle.getOriginalWidth());
    }
}