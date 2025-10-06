// Arkanoid/model/Ball.java
package model;

public class Ball extends MovableObject {

    private final double MAX_ANGLE = Math.toRadians(75);

    public Ball(double x, double y, double width, double height, double speed, double directionX, double directionY) {
        super(x, y, width, height, speed);
        this.directionX = directionX;
        this.directionY = directionY;
    }

    @Override
    public void update() {
        super.update();
        // Giới hạn bóng trong các cạnh màn hình
        // Xảy ra ở GameManager, nhưng có thể thêm logic ở đây nếu muốn Ball tự quản lý biên
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
            if (overlapX < overlapY) { // Va chạm ngang
                this.directionX *= -1;
                // Điều chỉnh vị trí để bóng không bị kẹt
                if (ballCenterX < objCenterX) { // Va chạm từ trái
                    this.x = obj.getX() - this.width;
                } else { // Va chạm từ phải
                    this.x = obj.getX() + obj.getWidth();
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
//                    double hitPos = (ballCenterX - obj.getX()) / obj.getWidth(); // 0-1
//                    // Điều chỉnh hướng X từ -1 (cực trái) đến 1 (cực phải)
//                    this.directionX = (hitPos - 0.5) * 2;
//                    if (Math.abs(this.directionX) < 0.2) { // Đảm bảo luôn có một chút hướng ngang
//                        this.directionX = (this.directionX >= 0 ? 0.2 : -0.2);
//                    }
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

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println("Ball at (" + (int)x + ", " + (int)y + ") speed: " + speed + ", dir: (" + directionX + ", " + directionY + ")");
    }
}