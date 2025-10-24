// Arkanoid/model/StrongBrick.java
package model;

public class StrongBrick extends Brick {

    public StrongBrick(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, 2, "Strong"); // Gạch mạnh có 2 máu
    }

    public StrongBrick(String imagePath, double x, double y, double width, double height, int health) {
        super(imagePath, x, y, width, height, 2, "Strong"); // Gạch mạnh có 2 máu
    }

    // Có thể override takeHit() hoặc update() nếu có hành vi đặc biệt
}