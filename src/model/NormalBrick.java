// Arkanoid/model/NormalBrick.java
package model;

public class NormalBrick extends Brick {

    public NormalBrick(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, 1); // Gạch thường có 1 máu
    }

    public NormalBrick(String imagePath, double x, double y, double width, double height, int health) {
        super(imagePath, x, y, width, height, health); // Gạch thường có 1 máu
    }

    // Có thể override takeHit() hoặc update() nếu có hành vi đặc biệt
    @Override
    public void update() {
        
    }
}