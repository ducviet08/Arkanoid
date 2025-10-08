// Arkanoid/model/ExpandPaddlePowerUp.java
package model;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final double EXPAND_AMOUNT = 50; // Lượng mở rộng thêm cho paddle

    public ExpandPaddlePowerUp(double x, double y, double width, double height, long duration) {
        super(x, y, width, height, FALL_SPEED, duration);
    }

    @Override
    public void applyEffect(Paddle paddle) {
        paddle.setWidth(paddle.getOriginalWidth() + EXPAND_AMOUNT);
        if (paddle.getX() + paddle.getWidth() > 800) {
            paddle.setX(800 - paddle.getWidth());
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        paddle.setWidth(paddle.getOriginalWidth());
    }
}