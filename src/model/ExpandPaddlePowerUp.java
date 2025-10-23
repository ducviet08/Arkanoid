// Arkanoid/model/ExpandPaddlePowerUp.java
package model;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final double EXPAND_AMOUNT = 50; // Lượng mở rộng thêm cho paddle

    public ExpandPaddlePowerUp(String imagePath, double x, double y, double width, double height, long duration) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
    }

    @Override
    public void applyEffect(Paddle paddle) {
        System.out.println("ExpandPaddle PowerUp activated!");
        paddle.setWidth(Paddle.ORIGINAL_WIDTH + EXPAND_AMOUNT);
        paddle.setImage("/images/paddle.png");
        // Đảm bảo paddle không vượt quá kích thước màn hình
        if (paddle.getX() + paddle.getWidth() > 800) {
            paddle.setX(800 - paddle.getWidth());
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        paddle.setImage("/images/paddle2.png");
        System.out.println("ExpandPaddle PowerUp deactivated!");
        paddle.resetWidth(); // Trở về kích thước ban đầu
    }
}