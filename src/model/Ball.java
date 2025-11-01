//// Arkanoid/model/Ball.java
//package model;
//
//public class Ball extends MovableObject {
//    private boolean active;
//    public static final double MAX_ANGLE = Math.toRadians(75);
//    public static final double ORIGINAL_SPEED = 2.5;
//    public static final double ORIGINAL_HEIGHT = 15;
//    public static final double ORIGINAL_WIDTH = 15;
//    public static final double ORIGINAL_MAX_SPEED = 5;
//    private double minSpeed;
//    private double maxSpeed;
//
//    public Ball(String imagePath, double x, double y, double directionX, double directionY) {
//        super(imagePath, x, y, ORIGINAL_WIDTH, ORIGINAL_HEIGHT, ORIGINAL_SPEED);
//        this.directionX = directionX;
//        this.directionY = directionY;
//        active = false;
//        resetSpeedLimits();
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        // Giới hạn bóng trong các cạnh màn hình
//        // Xảy ra ở GameManager, nhưng có thể thêm logic ở đây nếu muốn Ball tự quản lý biên
//    }
//
//    public void move(Paddle paddle, PowerUp activePowerUp) {
//        if (active) {
//            update();
//        } else if (paddle.isSticky() && activePowerUp instanceof StickyPaddlePowerUp stickyPaddle
//                && stickyPaddle.isStuck()) {
//            // kiểm tra nếu đang dính trên paddle trong StickyPaddle thì di chuyển theo vị trí của Paddle
////            x = paddle.getX() + stickyPaddle.getOffSetX();
////            y = paddle.getY() - height - 1;
//            double newX = paddle.getX() + (stickyPaddle.getRelativeOffset() * paddle.getWidth());
//
//            // Đảm bảo bóng luôn nằm *trên* paddle, ngay cả khi paddle co lại
//            // Căn newX để bóng không lòi ra bên trái paddle
//            if (newX < paddle.getX()) {
//                newX = paddle.getX();
//            }
//            // Căn newX để bóng không lòi ra bên phải paddle
//            if (newX + getWidth() > paddle.getX() + paddle.getWidth()) {
//                newX = paddle.getX() + paddle.getWidth() - getWidth();
//            }
//
//            setX(newX);
//            setY(paddle.getY() - getHeight()); // Đặt bóng ngay trên paddle
//        } else {
//            x = paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2;
//            y = paddle.getY() - paddle.getHeight() / 2;
//        }
//    }
//
//    public void bounceOff(GameObject obj, PowerUp activePowerUp) {
//        // Xác định va chạm từ phía nào để thay đổi hướng cho phù hợp
//        double ballCenterX = this.x + this.width / 2;
//        double ballCenterY = this.y + this.height / 2;
//        double objCenterX = obj.getX() + obj.getWidth() / 2;
//        double objCenterY = obj.getY() + obj.getHeight() / 2;
//
//        double overlapX = (this.width / 2 + obj.getWidth() / 2) - Math.abs(ballCenterX - objCenterX);
//        double overlapY = (this.height / 2 + obj.getHeight() / 2) - Math.abs(ballCenterY - objCenterY);
//
//        if (overlapX > 0 && overlapY > 0) {
//            if (overlapX < overlapY) { // Va chạm cạnh trái phải của paddle
//                this.directionX *= -1;
//                // Điều chỉnh vị trí để bóng không bị kẹt
//                if (ballCenterX < objCenterX) { // Va chạm từ trái
//                    this.x = obj.getX() - this.width;
//                    if (this.x < 0) {
//                        this.x = 0;
//                        this.directionX = 0;
//                        this.directionY = Math.abs(this.directionY);
//                    }
//                } else { // Va chạm từ phải
//                    this.x = obj.getX() + obj.getWidth();
//                    if (this.x + this.width > 800) {
//                        this.x = 800 - this.width;
//                        this.directionX = 0;
//                        this.directionY = Math.abs(this.directionY);
//                    }
//                }
//            } else { // Va chạm dọc
//                this.directionY *= -1;
//                // Điều chỉnh vị trí để bóng không bị kẹt
//                if (ballCenterY < objCenterY) { // Va chạm từ trên
//                    this.y = obj.getY() - this.height;
//                } else { // Va chạm từ dưới
//                    this.y = obj.getY() + obj.getHeight();
//                }
//
//                // Nếu va chạm với Paddle, điều chỉnh hướng X dựa trên vị trí va chạm
//                if (obj instanceof Paddle) {
//                    if (this.y < obj.getY()) { // chỉ xử lý khi va chạm mặt trên của paddle
//                        // xử lý trường hợp dính trên Paddle
//                        if (activePowerUp instanceof StickyPaddlePowerUp stickyPaddle && ((Paddle) obj).isSticky()) {
//                            stickyPaddle.onBallHitPaddle(this, (Paddle) obj);
//                        } else if (active) {
//                            // --- BẮT ĐẦU LOGIC MỚI ---
//
//                            // 1. Lấy paddle và vận tốc của nó
//                            Paddle paddle = (Paddle) obj;
//                            double paddleVelX = paddle.getVelocityX();
//
//                            // 2. Các hằng số để tinh chỉnh (bạn có thể thay đổi các giá trị này)
//
//                            // Tỷ lệ ảnh hưởng của paddle (0.0 - 1.0)
//                            // 0.25 có nghĩa là 25% vận tốc của paddle sẽ được "truyền" cho bóng
//                            final double PADDLE_INFLUENCE = 0.25;
//
//                            // 3. Tính góc nảy và hướng nảy cơ bản (như code cũ)
//                            ballCenterX = x + width / 2.0;
//                            double paddleCenterX = obj.getX() + obj.getWidth() / 2.0;
//                            double relativeIntersect = (ballCenterX - paddleCenterX) / (obj.getWidth() / 2.0);
//                            relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));
//                            double bounceAngle = relativeIntersect * MAX_ANGLE;
//
//                            double baseDirectionX = Math.sin(bounceAngle);
//                            double baseDirectionY = -Math.cos(bounceAngle);
//
//                            // 4. Lấy tốc độ hiện tại và tính vector vận tốc (velocity) cơ bản
//                            double currentSpeed = getSpeed();
//                            double baseVelX = baseDirectionX * currentSpeed;
//                            double baseVelY = baseDirectionY * currentSpeed;
//
//                            // 5. Tính "lực" truyền từ paddle
//                            double impartedVelX = paddleVelX * PADDLE_INFLUENCE;
//
//                            // 6. Tính vận tốc cuối cùng (cộng vận tốc cơ bản với lực truyền)
//                            double finalVelX = baseVelX + impartedVelX;
//                            double finalVelY = baseVelY; // Paddle chỉ ảnh hưởng theo trục X
//
//                            // 7. Tính TỐC ĐỘ (speed) mới từ vector vận tốc cuối cùng
//                            double newSpeed = Math.sqrt(finalVelX * finalVelX + finalVelY * finalVelY);
//
//                            // 8. Giới hạn tốc độ
//                            newSpeed = Math.max(this.minSpeed, Math.min(this.maxSpeed, newSpeed));
//
//                            // 9. Cập nhật tốc độ (speed) VÀ hướng (direction) mới
//                            setSpeed(newSpeed);
//
//                            // Chuẩn hóa vector hướng mới (chia cho tốc độ mới)
//                            this.directionX = finalVelX / newSpeed;
//                            this.directionY = finalVelY / newSpeed;
//
//                            // --- KẾT THÚC LOGIC MỚI ---
//                        }
//                    }
//
//                }
//            }
//        }
//    }
//
//    private double rotationAngle = 0;
//
//    public void updateRotation() {
//        rotationAngle += 5; // tốc độ xoay, có thể tăng/giảm
//        if (rotationAngle >= 360) rotationAngle -= 360;
//    }
//
//    public double getRotationAngle() {
//        return rotationAngle;
//    }
//
//    public void setOriginalSpeed() {
//        super.setSpeed(ORIGINAL_SPEED);
//    }
//
//    public void setOriginalHeight() {
//        super.setHeight(ORIGINAL_HEIGHT);
//    }
//
//    public void setOriginalWidth() {
//        super.setWidth(ORIGINAL_WIDTH);
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }
//
//    public void setMinSpeed(double minSpeed) {
//        this.minSpeed = minSpeed;
//    }
//
//    public void setMaxSpeed(double maxSpeed) {
//        this.maxSpeed = maxSpeed;
//    }
//
//    public void resetSpeedLimits() {
//        this.minSpeed = ORIGINAL_SPEED;
//        this.maxSpeed = ORIGINAL_MAX_SPEED;
//    }
//
//    public void clampSpeedToLimits() {
//        if (speed < minSpeed) {
//            setSpeed(minSpeed);
//        } else if (speed > maxSpeed) {
//            setSpeed(maxSpeed);
//        }
//    }
//
//    @Override
//    public void render() {
//        // Logic render đồ họa đã được chuyển sang Renderer.draw()
//        // Đây chỉ là một placeholder cho debug console
//        // System.out.println("Ball at (" + (int)x + ", " + (int)y + ") speed: " + speed + ", dir: (" + directionX + ", " + directionY + ")");
//    }
//}


// Arkanoid/model/Ball.java
package model;

import controller.SoundManager;

public class Ball extends MovableObject {
    private boolean active;
    public static final double MAX_ANGLE = Math.toRadians(75);
    public static final double ORIGINAL_SPEED = 2.5;
    public static final double ORIGINAL_RADIUS = 7.5;
    public static final double ORIGINAL_HEIGHT = 15;
    public static final double ORIGINAL_WIDTH = 15;

    // ===== HỆ THỐNG BOOST =====
    private static final double PADDLE_MOMENTUM_INFLUENCE = 0.25; // 25% vận tốc paddle
    private static final double INSTANT_BOOST_PER_HIT = 0.15; // +0.15 speed mỗi hit
    private static final long BOOST_DECAY_INTERVAL = 2000; // 2 giây mỗi stack
    private static final int MAX_BOOST_STACKS = 4; // Tối đa 4 stacks (+0.6 tốc độ)

    // Tracking boost
    private double currentBoost = 0.0;
    private long lastPaddleHitTime = 0;
    private int boostStacks = 0;

    // ===== TRACKING BASE SPEED TỪ POWERUP =====
    private double baseSpeedMultiplier = 1.0; // Hệ số từ PowerUp (FastBall, TinyBall, etc.)

    private double radius;
    private double rotationAngle = 0;

    public Ball(String imagePath, double x, double y, double directionX, double directionY) {
        super(imagePath, x, y, ORIGINAL_RADIUS * 2, ORIGINAL_RADIUS * 2, ORIGINAL_SPEED);
        this.radius = ORIGINAL_RADIUS;
        this.directionX = directionX;
        this.directionY = directionY;
        this.active = false;
    }

    @Override
    public void update() {
        updateBoostDecay();
        super.update();

        // Giới hạn bóng trong biên
        if (getCenterX() - radius <= 0) {
            if (active) SoundManager.playSound(SoundManager.SOUND_PADDLE_HIT);
            setCenterX(radius);
            directionX = Math.abs(directionX);
        }
        if (getCenterX() + radius >= 800) {
            if (active) SoundManager.playSound(SoundManager.SOUND_PADDLE_HIT);
            setCenterX(800 - radius);
            directionX = -Math.abs(directionX);
        }
        if (getCenterY() - radius <= 0) {
            if (active) SoundManager.playSound(SoundManager.SOUND_PADDLE_HIT);
            setCenterY(radius);
            directionY = Math.abs(directionY);
        }
    }

    /**
     * Giảm boost theo thời gian
     */
    private void updateBoostDecay() {
        if (boostStacks <= 0 || lastPaddleHitTime == 0) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long timeSinceLastHit = currentTime - lastPaddleHitTime;

        int stacksToDecay = (int) (timeSinceLastHit / BOOST_DECAY_INTERVAL);

        if (stacksToDecay > 0) {
            int oldStacks = boostStacks;
            boostStacks = Math.max(0, boostStacks - stacksToDecay);
            currentBoost = boostStacks * INSTANT_BOOST_PER_HIT;

            // Cập nhật thời gian
            long elapsedDecayTime = stacksToDecay * BOOST_DECAY_INTERVAL;
            lastPaddleHitTime = currentTime - (timeSinceLastHit - elapsedDecayTime);

            // Cập nhật speed = base + boost mới
            updateSpeedWithBoost();

            if (boostStacks <= 0) {
                lastPaddleHitTime = 0;
                currentBoost = 0.0;
                //System.out.println("Boost expired! (Speed: " + String.format("%.2f", getSpeed()) + ")");
            } else {
                System.out.println("Boost decayed: " + oldStacks + " → " + boostStacks +
                        " stacks (Speed: " + String.format("%.2f", getSpeed()) + ")");
            }
        }
    }

    /**
     * ===== QUAN TRỌNG: Cập nhật speed = base speed + boost =====
     * Base speed = ORIGINAL_SPEED * baseSpeedMultiplier (từ PowerUp)
     */
    private void updateSpeedWithBoost() {
        double baseSpeed = ORIGINAL_SPEED * baseSpeedMultiplier;
        setSpeed(baseSpeed + currentBoost);
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
        double ballCenterX = getCenterX();
        double ballCenterY = getCenterY();

        double closestX = Math.max(obj.getX(), Math.min(ballCenterX, obj.getX() + obj.getWidth()));
        double closestY = Math.max(obj.getY(), Math.min(ballCenterY, obj.getY() + obj.getHeight()));

        double distanceX = ballCenterX - closestX;
        double distanceY = ballCenterY - closestY;
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        if (distanceSquared <= radius * radius) {
            if (obj instanceof Paddle) {
                handlePaddleCollision((Paddle) obj, ballCenterX, ballCenterY, activePowerUp);
            } else {
                handleObjectCollision(obj, closestX, closestY, ballCenterX, ballCenterY);
            }
        }
    }

    private void handlePaddleCollision(Paddle paddle, double ballCenterX, double ballCenterY, PowerUp activePowerUp) {
        double paddleTop = paddle.getY();
        double paddleBottom = paddle.getY() + paddle.getHeight();
        double paddleLeft = paddle.getX();
        double paddleRight = paddle.getX() + paddle.getWidth();

        if (ballCenterY < paddleTop + paddle.getHeight() / 2 && directionY > 0) {
            // ===== VA CHẠM MẶT TRÊN PADDLE =====
            setCenterY(paddleTop - radius - 0.1);

            if (activePowerUp instanceof StickyPaddlePowerUp stickyPaddle && paddle.isSticky()) {
                long currentTime = System.currentTimeMillis();

                if (active && boostStacks < MAX_BOOST_STACKS) {
                    boostStacks++;
                    currentBoost = boostStacks * INSTANT_BOOST_PER_HIT;
                    lastPaddleHitTime = currentTime;
                    updateSpeedWithBoost();

                    System.out.println("🔥 Boost +1 → " + boostStacks + " stacks [STICKY]");
                }

                stickyPaddle.onBallHitPaddle(this, paddle);
                return;
            }

            if (active) {
                // === TÍNH GÓC BẬT ===
                double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
                double relativeIntersect = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2.0);
                relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));
                double bounceAngle = relativeIntersect * MAX_ANGLE;

                double baseDirectionX = Math.sin(bounceAngle);
                double baseDirectionY = -Math.cos(bounceAngle);

                // === MOMENTUM TRANSFER ===
                double paddleVelocityX = paddle.getVelocityX();
                double transferredMomentum = paddleVelocityX * PADDLE_MOMENTUM_INFLUENCE;

                // === BASE SPEED (chỉ từ PowerUp, KHÔNG bao gồm boost cũ) ===
                double baseSpeed = ORIGINAL_SPEED * baseSpeedMultiplier;

                // Vector vận tốc cơ bản
                double baseVelX = baseDirectionX * baseSpeed;
                double baseVelY = baseDirectionY * baseSpeed;

                // === THÊM BOOST STACK ===
                long currentTime = System.currentTimeMillis();

                if (boostStacks < MAX_BOOST_STACKS) {
                    boostStacks++;
                    currentBoost = boostStacks * INSTANT_BOOST_PER_HIT;
                    lastPaddleHitTime = currentTime;

                    System.out.println("🔥 Boost +1 → " + boostStacks + " stacks (+" +
                            String.format("%.2f", currentBoost) + " speed)");
                } else {
                    lastPaddleHitTime = currentTime;
                    System.out.println("🔥 Boost MAX (" + boostStacks + " stacks)");
                }

                // === TÍNH VẬN TỐC CUỐI CÙNG ===
                // Velocity = Base + Momentum
                double finalVelX = baseVelX + transferredMomentum;
                double finalVelY = baseVelY;

                double velocityMagnitude = Math.sqrt(finalVelX * finalVelX + finalVelY * finalVelY);

                // Speed = Velocity Magnitude + Boost
                double newSpeed = velocityMagnitude + currentBoost;

                // === CẬP NHẬT DIRECTION ===
                if (velocityMagnitude > 0.01) {
                    this.directionX = finalVelX / velocityMagnitude;
                    this.directionY = finalVelY / velocityMagnitude;
                } else {
                    this.directionX = baseDirectionX;
                    this.directionY = baseDirectionY;
                }

                setSpeed(newSpeed);

                /*System.out.println("⚡ Speed: " + String.format("%.2f", newSpeed) +
                        " (Base: " + String.format("%.2f", baseSpeed) +
                        " + Momentum: " + String.format("%.2f", Math.abs(transferredMomentum)) +
                        " + Boost: " + String.format("%.2f", currentBoost) + ")");*/
            }

        } else if (ballCenterX < paddleLeft && directionX > 0) {
            setCenterX(paddleLeft - radius - 0.1);
            directionX = -Math.abs(directionX);

        } else if (ballCenterX > paddleRight && directionX < 0) {
            setCenterX(paddleRight + radius + 0.1);
            directionX = Math.abs(directionX);

        } else if (ballCenterY > paddleBottom - paddle.getHeight() / 3) {
            setCenterY(paddleBottom + radius + 0.1);
            directionY = Math.abs(directionY);
        }
    }

    private void handleObjectCollision(GameObject obj, double closestX, double closestY,
                                       double ballCenterX, double ballCenterY) {
        double objLeft = obj.getX();
        double objRight = obj.getX() + obj.getWidth();
        double objTop = obj.getY();
        double objBottom = obj.getY() + obj.getHeight();

        boolean hitTop = Math.abs(closestY - objTop) < 0.1 && directionY > 0;
        boolean hitBottom = Math.abs(closestY - objBottom) < 0.1 && directionY < 0;
        boolean hitLeft = Math.abs(closestX - objLeft) < 0.1 && directionX > 0;
        boolean hitRight = Math.abs(closestX - objRight) < 0.1 && directionX < 0;

        boolean isCorner = (Math.abs(closestX - objLeft) < 0.1 || Math.abs(closestX - objRight) < 0.1) &&
                (Math.abs(closestY - objTop) < 0.1 || Math.abs(closestY - objBottom) < 0.1);

        if (isCorner) {
            double dx = ballCenterX - closestX;
            double dy = ballCenterY - closestY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                double overlap = radius - distance;
                setCenterX(ballCenterX + (dx / distance) * overlap);
                setCenterY(ballCenterY + (dy / distance) * overlap);
            }

            if (Math.abs(dx) > Math.abs(dy)) {
                directionX = -directionX;
            } else {
                directionY = -directionY;
            }
        } else if (hitTop || hitBottom) {
            if (hitTop) {
                setCenterY(objTop - radius - 0.1);
            } else {
                setCenterY(objBottom + radius + 0.1);
            }
            directionY = -directionY;

        } else if (hitLeft || hitRight) {
            if (hitLeft) {
                setCenterX(objLeft - radius - 0.1);
            } else {
                setCenterX(objRight + radius + 0.1);
            }
            directionX = -directionX;
        }
    }

    // ===== HELPER METHODS =====

    public double getCenterX() {
        return x + radius;
    }

    public double getCenterY() {
        return y + radius;
    }

    public void setCenterX(double centerX) {
        this.x = centerX - radius;
    }

    public void setCenterY(double centerY) {
        this.y = centerY - radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.width = radius * 2;
        this.height = radius * 2;
    }

    public void updateRotation() {
        rotationAngle += 5;
        if (rotationAngle >= 360) rotationAngle -= 360;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    /**
     * ===== SET SPEED - CẬP NHẬT BASE MULTIPLIER TỪ POWERUP =====
     */
    @Override
    public void setSpeed(double speed) {
        // Khi PowerUp gọi setSpeed(), tính lại baseSpeedMultiplier
        // Nhưng chỉ khi KHÔNG có boost active
        if (boostStacks == 0) {
            baseSpeedMultiplier = speed / ORIGINAL_SPEED;
        }
        super.setSpeed(speed);
    }

    public void setOriginalSpeed() {
        baseSpeedMultiplier = 1.0;
        updateSpeedWithBoost();
    }

    public void setOriginalSize() {
        setRadius(ORIGINAL_RADIUS);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean wasActive = this.active;
        this.active = active;

        if (wasActive && !active) {
            System.out.println("🔄 Ball deactivated - Resetting boost");
            resetBoost();
        }
    }

    /**
     * Reset boost về 0
     */
    public void resetBoost() {
        int oldStacks = boostStacks;

        boostStacks = 0;
        currentBoost = 0.0;
        lastPaddleHitTime = 0;

        // Cập nhật speed về base (không có boost)
        updateSpeedWithBoost();

        /*if (oldStacks > 0) {
            System.out.println("🔄 Boost reset: " + oldStacks + " → 0 stacks");
        }*/
    }

    public int getBoostStacks() {
        return boostStacks;
    }

    public double getCurrentBoost() {
        return currentBoost;
    }

    /**
     * ===== SET BASE SPEED MULTIPLIER (cho PowerUp) =====
     */
    public void setBaseSpeedMultiplier(double multiplier) {
        this.baseSpeedMultiplier = multiplier;
        updateSpeedWithBoost();
        /*System.out.println("📊 Base speed multiplier: " + String.format("%.2f", multiplier) +
                " (Speed: " + String.format("%.2f", getSpeed()) + ")");*/
    }

    @Override
    public void setHeight(double height) {
        super.setHeight(height);
        this.radius = height / 2;
    }

    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        this.radius = width / 2;
    }

    @Override
    public void render() {
    }

    public String getImagePath() {
        return this.path;
    }
}