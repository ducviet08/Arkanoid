// Arkanoid/model/PowerUp.java
package model;

public abstract class PowerUp extends MovableObject {
    protected long duration; // Thời gian hiệu lực của PowerUp (miligiây)
    protected static final double FALL_SPEED = 2;

    public PowerUp(String imagePath, double x, double y, double width, double height, double speed, long duration) {
        super(imagePath, x, y, width, height, speed);
        this.directionY = 1; // PowerUp luôn rơi xuống
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public abstract void applyEffect(Paddle paddle);
    public abstract void removeEffect(Paddle paddle);

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println("PowerUp (" + this.getClass().getSimpleName() + ") at (" + (int)x + ", " + (int)y + ")");
    }
}