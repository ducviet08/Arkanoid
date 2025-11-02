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

    @Override
    public void takeHit() {
        health--;
        System.out.println(" Brick hit! Health: " + health);
        if (health == 2) {
            super.setPath("/images/brick10-2.jpg");
            super.setImage("/images/brick10-2.jpg");
        } else if (health == 1) {
            super.setPath("/images/brick10-1.png");
            super.setImage("/images/brick10-1.png");
        }
    }
}