// Arkanoid/Paddle.java
package Arkanoid.model.paddle;

import Arkanoid.model.powerup.PowerUp;
import Arkanoid.model.base.MovableObject;

public class Paddle extends MovableObject {
    public static final double ORIGINAL_WIDTH = 100;// Kích thước ban đầu của paddle
    public static final double ORIGINAL_HEIGHT = 20;
    public static final double ORIGINAL_SPEED = 5;

    private double leftBoder = 0;   // Biên bên trái của Paddle
    private double rightBoder = 800;
    private boolean sticky = false;

    public Paddle(String imagePath, double x, double y) {
        super(imagePath, x, y, ORIGINAL_WIDTH, ORIGINAL_HEIGHT, ORIGINAL_SPEED);
    }

    public void moveLeft() {
        this.directionX = -1;
    }

    public void moveRight() {
        this.directionX = 1;
    }

    public void stop() {
        this.directionX = 0;
    }

    @Override
    public void update() {
        super.update();
        // Giới hạn Paddle trong các cạnh màn hình (800 là WIDTH của game)
        if (x < leftBoder) {
            x = leftBoder;
        }
        if (x + width > rightBoder) {
            x = rightBoder - width;
        }
    }

    public void applyPowerUp(PowerUp powerUp) {
        powerUp.applyEffect(this);
    }

    public void resetWidth() {
        super.setWidth(ORIGINAL_WIDTH);
    }

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println("Paddle at (" + (int)x + ", " + (int)y + "), width: " + (int)width + ", speed: " + speed + ", dirX: " + directionX);
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public double getRightBoder() {
        return rightBoder;
    }

    public double getLeftBoder() {
        return leftBoder;
    }

    public void setLeftBoder(double leftBoder) {
        this.leftBoder = leftBoder;
    }

    public void setRightBoder(double rightBoder) {
        this.rightBoder = rightBoder;
    }

    public double getVelocityX() {
        return this.directionX * this.speed;
    }
}