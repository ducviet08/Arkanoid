// Arkanoid/model/ExpandPaddlePowerUp.java
package model;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final double EXPAND_AMOUNT = 50; // Lượng mở rộng thêm cho paddle
    private static final double FALL_SPEED = 2; // Tốc độ rơi của power-up

    public ExpandPaddlePowerUp(double x, double y, double width, double height, long duration) {
        super(x, y, width, height, FALL_SPEED, duration);
    }

    @Override
    public void applyEffect(Paddle paddle) {
        System.out.println("ExpandPaddle PowerUp activated!");
        paddle.setWidth(paddle.getWidth() + EXPAND_AMOUNT);
        // Đảm bảo paddle không vượt quá kích thước màn hình
        if (paddle.getX() + paddle.getWidth() > 800) {
            paddle.setX(800 - paddle.getWidth());
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        System.out.println("ExpandPaddle PowerUp deactivated!");
        paddle.resetWidth(); // Trở về kích thước ban đầu
    }
}