// Arkanoid/StrongBrick.java
package Arkanoid.model.brick;

public class StrongBrick extends Brick {

    public StrongBrick(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, 3); // Gạch mạnh có 3 máu
    }

    public StrongBrick(String imagePath, double x, double y, double width, double height,int health) {
        super(imagePath, x, y, width, height, health); // Chuyền số máu còn lại dành cho loadGame
    }

    // Có thể override takeHit() hoặc update() nếu có hành vi đặc biệt
}