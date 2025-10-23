// Arkanoid/model/Ball.java
package model;

public class Ball extends MovableObject {
    private boolean active;
    private final double MAX_ANGLE = Math.toRadians(75);
    public static final double ORIGINAL_SPEED = 3.5;
    public static final double ORIGINAL_HEIGHT = 15;
    public static final double ORIGINAL_WIDTH = 15;

    public Ball(String imagePath, double x, double y, double directionX, double directionY) {
        super(imagePath, x, y, ORIGINAL_WIDTH, ORIGINAL_HEIGHT, ORIGINAL_SPEED);
        this.directionX = directionX;
        this.directionY = directionY;
        active = false;
    }

    @Override
    public void update() {
            super.update();
        // Giới hạn bóng trong các cạnh màn hình
        // Xảy ra ở GameManager, nhưng có thể thêm logic ở đây nếu muốn Ball tự quản lý biên
    }

    public void move(Paddle paddle) {
        if (active) {
            update();
        } else {
            x = paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2;
            y = paddle.getY() - paddle.getHeight() / 2;
        }
    }

    public void bounceOff(GameObject obj) {
        // Xác định va chạm từ phía nào để thay đổi hướng cho phù hợp
        double ballCenterX = this.x + this.width / 2;
        double ballCenterY = this.y + this.height / 2;
        double objCenterX = obj.getX() + obj.getWidth() / 2;
        double objCenterY = obj.getY() + obj.getHeight() / 2;

        double overlapX = (this.width / 2 + obj.getWidth() / 2) - Math.abs(ballCenterX - objCenterX);
        double overlapY = (this.height / 2 + obj.getHeight() / 2) - Math.abs(ballCenterY - objCenterY);

        if (overlapX > 0 && overlapY > 0) {
            if (overlapX < overlapY) { // Va chạm cạnh trái phải của paddle
                this.directionX *= -1;
                // Điều chỉnh vị trí để bóng không bị kẹt
                if (ballCenterX < objCenterX) { // Va chạm từ trái
                    this.x = obj.getX() - this.width;
                    if (this.x < 0) {
                        this.x = 0;
                        this.directionX = 0;
                        this.directionY = Math.abs(this.directionY);
                    }
                } else { // Va chạm từ phải
                    this.x = obj.getX() + obj.getWidth();
                    if (this.x + this.width > 800) {
                        this.x = 800 - this.width;
                        this.directionX = 0;
                        this.directionY = Math.abs(this.directionY);
                    }
                }
            } else { // Va chạm dọc
                this.directionY *= -1;
                // Điều chỉnh vị trí để bóng không bị kẹt
                if (ballCenterY < objCenterY) { // Va chạm từ trên
                    this.y = obj.getY() - this.height;
                } else { // Va chạm từ dưới
                    this.y = obj.getY() + obj.getHeight();
                }

                // Nếu va chạm với Paddle, điều chỉnh hướng X dựa trên vị trí va chạm
                if (obj instanceof Paddle) {
                    if(this.y < obj.getY()) { // chỉ xử lý khi va chạm mặt trên của paddle
                        ballCenterX = x + width / 2.0;
                        double paddleCenterX = obj.getX() + obj.getWidth() / 2.0;
                        double relativeIntersect = (ballCenterX - paddleCenterX) / (obj.getWidth() / 2.0);

                        // Giới hạn lại giá trị [-1, 1]
                        relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));

                        // Góc bật (0 = giữa, ±MAX_ANGLE = hai mép)
                        double bounceAngle = relativeIntersect * MAX_ANGLE;

                        // Tính lại hướng bóng
                        directionX = Math.sin(bounceAngle);
                        directionY = -Math.cos(bounceAngle);
                    }
                }
            }
        }
    }

    public void setOriginalSpeed() {
        super.setSpeed(ORIGINAL_SPEED);
    }

    public void setOriginalHeight() {
        super.setHeight(ORIGINAL_HEIGHT);
    }

    public void setOriginalWidth() {
        super.setWidth(ORIGINAL_WIDTH);
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println("Ball at (" + (int)x + ", " + (int)y + ") speed: " + speed + ", dir: (" + directionX + ", " + directionY + ")");
    }
}