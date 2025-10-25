package model;

import java.util.List;

public class ExplosiveBrick extends Brick {
    private int explosionDamage;

    public ExplosiveBrick(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, 1);
        this.explosionDamage = 1;
    }

    public ExplosiveBrick(String imagePath, double x, double y, double width, double height, int health) {
        super(imagePath, x, y, width, height, health);
        this.explosionDamage = 1;
    }

    public ExplosiveBrick(String imagePath, double x, double y, double width, double height, int health, int explosionDamage) {
        super(imagePath, x, y, width, height, health);
        this.explosionDamage = explosionDamage;
    }

    public int getExplosionDamage() {
        return explosionDamage;
    }

    public void explode(List<Brick> bricks) {
        for (Brick brick : bricks) {
            if (brick == this || brick.isDestroyed()) {
                continue;
            }

            if (isNeighbor(brick)) {
                for (int i = 0; i < explosionDamage; i++) {
                    brick.takeHit();
                }
                System.out.println(" Brick hit! Health: " + brick.getHealth());
            }
        }
    }

    /**
     * Kiểm tra brick có phải là hàng xóm (xung quanh) không.
     */
    private boolean isNeighbor(Brick other) {
        double gap = 5;
        double brickWidth = 80;
        double brickHeight = 25;

        // Tính vị trí tương đối
        double dx = Math.abs(this.x - other.getX());
        double dy = Math.abs(this.y - other.getY());

        // Khoảng cách ngang giữa 2 brick liền kề
        double horizontalDistance = brickWidth + gap;

        // Khoảng cách dọc giữa 2 brick liền kề
        double verticalDistance = brickHeight + gap;

        // Sai số rất nhỏ
        double tolerance = 0.1;

        // trái hoặc phải
        if (dy < tolerance && Math.abs(dx - horizontalDistance) < tolerance) {
            return true;
        }

        // trên hoặc dưới
        if (dx < tolerance && Math.abs(dy - verticalDistance) < tolerance) {
            return true;
        }

        // 4 góc
        if (Math.abs(dx - horizontalDistance) < tolerance &&
                Math.abs(dy - verticalDistance) < tolerance) {
            return true;
        }

        return false;
    }
}
