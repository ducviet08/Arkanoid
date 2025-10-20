// Arkanoid/model/StrongBrick.java
package model;

public class StrongBrick extends Brick {

    public StrongBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 3, "Strong"); // Gạch mạnh có 2 máu
    }

    // Có thể override takeHit() hoặc update() nếu có hành vi đặc biệt
}