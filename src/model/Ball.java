// Arkanoid/model/Ball.java
package model;

public class Ball extends MovableObject {
    private boolean active;
    public static final double MAX_ANGLE = Math.toRadians(75);
    public static final double ORIGINAL_SPEED = 2.5;
    public static final double ORIGINAL_HEIGHT = 15;
    public static final double ORIGINAL_WIDTH = 15;
    public static final double ORIGINAL_MAX_SPEED = 5;
    private double minSpeed;
    private double maxSpeed;

    public Ball(String imagePath, double x, double y, double directionX, double directionY) {
        super(imagePath, x, y, ORIGINAL_WIDTH, ORIGINAL_HEIGHT, ORIGINAL_SPEED);
        this.directionX = directionX;
        this.directionY = directionY;
        active = false;
        resetSpeedLimits();
    }

    @Override
    public void update() {
        super.update();
        // Giới hạn bóng trong các cạnh màn hình
        // Xảy ra ở GameManager, nhưng có thể thêm logic ở đây nếu muốn Ball tự quản lý biên
    }

    public void move(Paddle paddle, PowerUp activePowerUp) {
        if (active) {
            update();
        } else if (paddle.isSticky() && activePowerUp instanceof StickyPaddlePowerUp stickyPaddle
                && stickyPaddle.isStuck()) {
            // kiểm tra nếu đang dính trên paddle trong StickyPaddle thì di chuyển theo vị trí của Paddle
//            x = paddle.getX() + stickyPaddle.getOffSetX();
//            y = paddle.getY() - height - 1;
            double newX = paddle.getX() + (stickyPaddle.getRelativeOffset() * paddle.getWidth());

            // Đảm bảo bóng luôn nằm *trên* paddle, ngay cả khi paddle co lại
            // Căn newX để bóng không lòi ra bên trái paddle
            if (newX < paddle.getX()) {
                newX = paddle.getX();
            }
            // Căn newX để bóng không lòi ra bên phải paddle
            if (newX + getWidth() > paddle.getX() + paddle.getWidth()) {
                newX = paddle.getX() + paddle.getWidth() - getWidth();
            }

            setX(newX);
            setY(paddle.getY() - getHeight()); // Đặt bóng ngay trên paddle
        } else {
            x = paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2;
            y = paddle.getY() - paddle.getHeight() / 2;
        }
    }

    public void bounceOff(GameObject obj, PowerUp activePowerUp) {
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
                    if (this.y < obj.getY()) { // chỉ xử lý khi va chạm mặt trên của paddle
                        // xử lý trường hợp dính trên Paddle
                        if (activePowerUp instanceof StickyPaddlePowerUp stickyPaddle && ((Paddle) obj).isSticky()) {
                            stickyPaddle.onBallHitPaddle(this, (Paddle) obj);
                        } else if (active) {
                            // --- BẮT ĐẦU LOGIC MỚI ---

                            // 1. Lấy paddle và vận tốc của nó
                            Paddle paddle = (Paddle) obj;
                            double paddleVelX = paddle.getVelocityX();

                            // 2. Các hằng số để tinh chỉnh (bạn có thể thay đổi các giá trị này)

                            // Tỷ lệ ảnh hưởng của paddle (0.0 - 1.0)
                            // 0.25 có nghĩa là 25% vận tốc của paddle sẽ được "truyền" cho bóng
                            final double PADDLE_INFLUENCE = 0.25;

                            // 3. Tính góc nảy và hướng nảy cơ bản (như code cũ)
                            ballCenterX = x + width / 2.0;
                            double paddleCenterX = obj.getX() + obj.getWidth() / 2.0;
                            double relativeIntersect = (ballCenterX - paddleCenterX) / (obj.getWidth() / 2.0);
                            relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));
                            double bounceAngle = relativeIntersect * MAX_ANGLE;

                            double baseDirectionX = Math.sin(bounceAngle);
                            double baseDirectionY = -Math.cos(bounceAngle);

                            // 4. Lấy tốc độ hiện tại và tính vector vận tốc (velocity) cơ bản
                            double currentSpeed = getSpeed();
                            double baseVelX = baseDirectionX * currentSpeed;
                            double baseVelY = baseDirectionY * currentSpeed;

                            // 5. Tính "lực" truyền từ paddle
                            double impartedVelX = paddleVelX * PADDLE_INFLUENCE;

                            // 6. Tính vận tốc cuối cùng (cộng vận tốc cơ bản với lực truyền)
                            double finalVelX = baseVelX + impartedVelX;
                            double finalVelY = baseVelY; // Paddle chỉ ảnh hưởng theo trục X

                            // 7. Tính TỐC ĐỘ (speed) mới từ vector vận tốc cuối cùng
                            double newSpeed = Math.sqrt(finalVelX * finalVelX + finalVelY * finalVelY);

                            // 8. Giới hạn tốc độ
                            newSpeed = Math.max(this.minSpeed, Math.min(this.maxSpeed, newSpeed));

                            // 9. Cập nhật tốc độ (speed) VÀ hướng (direction) mới
                            setSpeed(newSpeed);

                            // Chuẩn hóa vector hướng mới (chia cho tốc độ mới)
                            this.directionX = finalVelX / newSpeed;
                            this.directionY = finalVelY / newSpeed;

                            // --- KẾT THÚC LOGIC MỚI ---
                        }
                    }

                }
            }
        }
    }

    private double rotationAngle = 0;

    public void updateRotation() {
        rotationAngle += 5; // tốc độ xoay, có thể tăng/giảm
        if (rotationAngle >= 360) rotationAngle -= 360;
    }

    public double getRotationAngle() {
        return rotationAngle;
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

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void resetSpeedLimits() {
        this.minSpeed = ORIGINAL_SPEED;
        this.maxSpeed = ORIGINAL_MAX_SPEED;
    }

    public void clampSpeedToLimits() {
        if (speed < minSpeed) {
            setSpeed(minSpeed);
        } else if (speed > maxSpeed) {
            setSpeed(maxSpeed);
        }
    }

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println("Ball at (" + (int)x + ", " + (int)y + ") speed: " + speed + ", dir: (" + directionX + ", " + directionY + ")");
    }
}