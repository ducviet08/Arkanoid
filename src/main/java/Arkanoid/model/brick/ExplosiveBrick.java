package Arkanoid.model.brick;

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

    // Tìm kiếm và giảm máu của các brick xung quang, bao gồm cả 4 góc và 4 hướng
    public void explode(List<Brick> bricks, Brick explosiveBrick) {
        for (Brick brick : bricks) {
            if (brick == explosiveBrick || brick.isDestroyed()) {
                continue;
            }

            if (isNeighbor(brick, explosiveBrick)) {
                for (int i = 0; i < explosionDamage; i++) {
                    brick.takeHit();
                }
                // System.out.println(brick.getType() + " Brick hit! Health: " + brick.getHealth());
                if (brick.isDestroyed() && brick instanceof ExplosiveBrick) {
                    explode(bricks, brick);
                }
            }
        }
    }

    /**
     * Kiểm tra brick có phải là hàng xóm (xung quanh) không.
     */
    private boolean isNeighbor(Brick other, Brick explosiveBrick) {
        double gap = 5;
        double brickWidth = 80;
        double brickHeight = 25;

        // Tính vị trí tương đối
        double dx = Math.abs(explosiveBrick.getX() - other.getX());
        double dy = Math.abs(explosiveBrick.getY() - other.getY());

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
