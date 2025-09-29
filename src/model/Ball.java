// Arkanoid/model/Ball.java
package model;

public class Ball extends MovableObject {

    public Ball(double x, double y, double width, double height, double speed, double directionX, double directionY) {
        super(x, y, width, height, speed);
        this.directionX = directionX;
        this.directionY = directionY;
    }

    @Override
    public void update() {
    }

    public void bounceOff(GameObject obj) {

    }

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println("Ball at (" + (int)x + ", " + (int)y + ") speed: " + speed + ", dir: (" + directionX + ", " + directionY + ")");
    }
}