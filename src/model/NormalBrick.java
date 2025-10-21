// Arkanoid/model/NormalBrick.java
package model;

public class NormalBrick extends Brick {

    public NormalBrick(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, 1, "Normal"); // Gạch thường có 1 máu
    }

    // Có thể override takeHit() hoặc update() nếu có hành vi đặc biệt
    // Ví dụ: nổ tung, thay đổi màu sắc khi bị đánh
}