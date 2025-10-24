package model;

public class ExplosiveBrick extends Brick {
    private int explosionDamage;

    public ExplosiveBrick(double x, double y, double width, double height) {
        super(x, y, width, heighd, 1, "Explosive");
        this.explosionDamage = 1;
    }

    public ExplosiveBrick(double x, double y, double width, double height, int health) {
        super(x, y, width, height, health, "Explosive");
        this.explosionDamage = 1;
    }

    @Override
    public void takeHit() {
        health--;

    }

    public void explode(List<Brick> allBricks) {
        System.out.println("EXPLOSION at (" + (int)x + ", " + (int)y + ")!");

        for (Brick brick : allBricks) {
            if (brick == this || brick.isDestroyed()) {
                continue;
            }

            if (isNeighbor(brick)) {
                // Gây sát thương
                for (int i = 0; i < explosionDamage; i++) {
                    brick.takeHit();
                }
                System.out.println("  → Damaged " + brick.getType() +
                        " brick at (" + (int)brick.getX() + ", " + (int)brick.getY() + ")");
            }
        }
    }

    /**
     * Kiểm tra brick có phải là hàng xóm (xung quanh) không.
     */
    private boolean isNeighbor(Brick other) {
        double gap = 5; // Khoảng cách giữa các brick
        double brickWidth = 80;
        double brickHeight = 25;

        // Tính vị trí tương đối
        double dx = Math.abs(this.x - other.getX());
        double dy = Math.abs(this.y - other.getY());

        // Khoảng cách ngang giữa 2 brick liền kề = width + gap
        double horizontalDistance = brickWidth + gap; // 80 + 5 = 85

        // Khoảng cách dọc giữa 2 brick liền kề = height + gap
        double verticalDistance = brickHeight + gap; // 25 + 5 = 30

        // Cho phép sai số nhỏ (0.1) để tránh lỗi floating point
        double tolerance = 0.1;

        // Kiểm tra các trường hợp:

        // 1. Brick cùng hàng (trái hoặc phải)
        if (Math.abs(dy) < tolerance && Math.abs(dx - horizontalDistance) < tolerance) {
            return true;
        }

        // 2. Brick cùng cột (trên hoặc dưới)
        if (Math.abs(dx) < tolerance && Math.abs(dy - verticalDistance) < tolerance) {
            return true;
        }

        // 3. Brick ở góc chéo (4 góc)
        if (Math.abs(dx - horizontalDistance) < tolerance &&
                Math.abs(dy - verticalDistance) < tolerance) {
            return true;
        }

        return false;
    }
}
