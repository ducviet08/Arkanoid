// Arkanoid/model/Paddle.java
package model;

public class Paddle extends MovableObject {
    private double originalWidth; // Kích thước ban đầu của paddle

    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, speed);
        this.originalWidth = width;
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
        if (x < 0) {
            x = 0;
        }
        if (x + width > 800) {
            x = 800 - width;
        }
    }

    public void applyPowerUp(PowerUp powerUp) {
        powerUp.applyEffect(this);
    }

    public void resetWidth() {
        this.width = originalWidth;
    }

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println("Paddle at (" + (int)x + ", " + (int)y + "), width: " + (int)width + ", speed: " + speed + ", dirX: " + directionX);
    }
}