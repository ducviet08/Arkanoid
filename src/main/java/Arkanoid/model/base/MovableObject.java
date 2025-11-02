// Arkanoid/MovableObject.java
package Arkanoid.model.base;

public abstract class MovableObject extends GameObject {
    protected double speed;
    protected double directionX, directionY; // -1 for left/up, 1 for right/down, 0 for stationary

    public MovableObject(String imagePath, double x, double y, double width, double height, double speed) {
        super(imagePath, x, y, width, height);
        this.speed = speed;
        this.directionX = 0;
        this.directionY = 0;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    @Override
    public void update() {
        x += speed * directionX;
        y += speed * directionY;
    }
}