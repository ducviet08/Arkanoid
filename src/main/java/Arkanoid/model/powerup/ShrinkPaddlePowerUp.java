// Arkanoid/ExpandPaddlePowerUp.java
package Arkanoid.model.powerup;

import Arkanoid.model.paddle.Paddle;

public class ShrinkPaddlePowerUp extends PowerUp {
    private static final double SHRINK_AMOUNT = 50; // Lượng mở rộng thêm cho paddle

    public ShrinkPaddlePowerUp(String pathImage, double x, double y, double width, double height, long duration) {
        super(pathImage, x, y, width, height, FALL_SPEED, duration);
    }

    @Override
    public void applyEffect(Paddle paddle) {
        paddle.setWidth(Paddle.ORIGINAL_WIDTH - SHRINK_AMOUNT);
    }

    @Override
    public void removeEffect(Paddle paddle) {
        paddle.resetWidth();
        //paddle.setImage(paddleImage);
    }
}