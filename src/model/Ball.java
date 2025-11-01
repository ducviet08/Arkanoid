// Arkanoid/model/Ball.java
package model;

import controller.SoundManager;

/**
 * Ball - Quả bóng trong game
 *
 * CẢI TIẾN MỚI:
 * ✅ Va chạm Ball-Ball với vật lý elastic collision
 * ✅ Thêm method getBaseSpeedMultiplier() để copy cho bóng mới
 * ✅ Hỗ trợ StickyPaddle với nhiều bóng
 */
public class Ball extends MovableObject {
    private boolean active;
    public static final double MAX_ANGLE = Math.toRadians(75);
    public static final double ORIGINAL_SPEED = 2.5;
    public static final double ORIGINAL_RADIUS = 7.5;
    public static final double ORIGINAL_HEIGHT = 15;
    public static final double ORIGINAL_WIDTH = 15;

    // ===== HỆ THỐNG BOOST =====
    private static final double PADDLE_MOMENTUM_INFLUENCE = 0.25;
    private static final double INSTANT_BOOST_PER_HIT = 0.15;
    private static final long BOOST_DECAY_INTERVAL = 2000;
    private static final int MAX_BOOST_STACKS = 4;

    private double currentBoost = 0.0;
    private long lastPaddleHitTime = 0;
    private int boostStacks = 0;

    private double baseSpeedMultiplier = 1.0;
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

    private void updateBoostDecay() {
        if (boostStacks <= 0 || lastPaddleHitTime == 0) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long timeSinceLastHit = currentTime - lastPaddleHitTime;

        int stacksToDecay = (int) (timeSinceLastHit / BOOST_DECAY_INTERVAL);

        if (stacksToDecay > 0) {
            boostStacks = Math.max(0, boostStacks - stacksToDecay);
            currentBoost = boostStacks * INSTANT_BOOST_PER_HIT;

            long elapsedDecayTime = stacksToDecay * BOOST_DECAY_INTERVAL;
            lastPaddleHitTime = currentTime - (timeSinceLastHit - elapsedDecayTime);

            updateSpeedWithBoost();

            if (boostStacks <= 0) {
                lastPaddleHitTime = 0;
                currentBoost = 0.0;
            }
        }
    }

    private void updateSpeedWithBoost() {
        double baseSpeed = ORIGINAL_SPEED * baseSpeedMultiplier;
        setSpeed(baseSpeed + currentBoost);
    }

    public void move(Paddle paddle, PowerUp activePowerUp) {
        if (active) {
            update();
        } else if (paddle.isSticky() && activePowerUp instanceof StickyPaddlePowerUp stickyPaddle
                && stickyPaddle.isStuckBall(this)) {
            // ⭐ StickyPaddle mới: Kiểm tra bóng CỤ THỂ có dính không
            double newX = paddle.getX() + (stickyPaddle.getRelativeOffset(this) * paddle.getWidth());
            setX(newX);
            setY(paddle.getY() - getHeight());
        } else {
            x = paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2;
            y = paddle.getY() - paddle.getHeight() / 2;
        }
    }

    // ===== 🆕 VA CHẠM BALL - BALL =====
    /**
     * Kiểm tra và xử lý va chạm với bóng khác
     * Sử dụng Elastic Collision Physics
     */
    // ===== 🆕 VA CHẠM BALL - BALL =====
    /**
     * Kiểm tra và xử lý va chạm với bóng khác
     *
     * LOGIC MỚI:
     * - Nếu CẢ HAI bóng đều active → va chạm elastic bình thường
     * - Nếu một bóng DÍNH (active=false), bóng còn lại ACTIVE:
     *   → Bóng active bật ra (phản xạ), bóng dính vẫn dính
     */
    public void checkBallCollision(Ball other) {
        if (other == this) {
            return;
        }

        double dx = this.getCenterX() - other.getCenterX();
        double dy = this.getCenterY() - other.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDistance = this.radius + other.radius;

        if (distance < minDistance && distance > 0) {

            // ===== TRƯỜNG HỢP 1: CẢ HAI BÓNG ACTIVE =====
            if (this.isActive() && other.isActive()) {
                SoundManager.playSound(SoundManager.SOUND_PADDLE_HIT);

                // 1. TÁCH BÓNG RA
                double overlap = minDistance - distance;
                double nx = dx / distance; // Normal vector
                double ny = dy / distance;

                this.setCenterX(this.getCenterX() + nx * overlap / 2);
                this.setCenterY(this.getCenterY() + ny * overlap / 2);
                other.setCenterX(other.getCenterX() - nx * overlap / 2);
                other.setCenterY(other.getCenterY() - ny * overlap / 2);

                // 2. TÍNH VẬN TỐC MỚI (Elastic Collision)
                double v1x = this.directionX * this.speed;
                double v1y = this.directionY * this.speed;
                double v2x = other.directionX * other.speed;
                double v2y = other.directionY * other.speed;

                double dvx = v1x - v2x;
                double dvy = v1y - v2y;
                double dvn = dvx * nx + dvy * ny;

                if (dvn >= 0) {
                    return; // Đang tách ra
                }

                double impulse = dvn;

                v1x -= impulse * nx;
                v1y -= impulse * ny;
                v2x += impulse * nx;
                v2y += impulse * ny;

                // 3. CẬP NHẬT DIRECTION VÀ SPEED
                double newSpeed1 = Math.sqrt(v1x * v1x + v1y * v1y);
                if (newSpeed1 > 0.01) {
                    this.directionX = v1x / newSpeed1;
                    this.directionY = v1y / newSpeed1;
                    this.setSpeed(newSpeed1);
                }

                double newSpeed2 = Math.sqrt(v2x * v2x + v2y * v2y);
                if (newSpeed2 > 0.01) {
                    other.directionX = v2x / newSpeed2;
                    other.directionY = v2y / newSpeed2;
                    other.setSpeed(newSpeed2);
                }
            }

            // ===== TRƯỜNG HỢP 2: MỘT BÓNG DÍNH, MỘT BÓNG ACTIVE =====
            else if (this.isActive() && !other.isActive()) {
                // THIS active, OTHER dính → Bật THIS ra
                SoundManager.playSound(SoundManager.SOUND_PADDLE_HIT);

                double overlap = minDistance - distance;
                double nx = dx / distance;
                double ny = dy / distance;

                // Chỉ đẩy bóng ACTIVE ra
                this.setCenterX(this.getCenterX() + nx * overlap);
                this.setCenterY(this.getCenterY() + ny * overlap);

                // Phản xạ hướng bóng active (bounce off)
                double dotProduct = this.directionX * nx + this.directionY * ny;

                // Chỉ phản xạ nếu đang đi VÀO bóng dính
                if (dotProduct < 0) {
                    this.directionX -= 2 * dotProduct * nx;
                    this.directionY -= 2 * dotProduct * ny;
                }
            }

            else if (!this.isActive() && other.isActive()) {
                // THIS dính, OTHER active → Bật OTHER ra
                SoundManager.playSound(SoundManager.SOUND_PADDLE_HIT);

                double overlap = minDistance - distance;
                double nx = dx / distance;
                double ny = dy / distance;

                // Chỉ đẩy bóng ACTIVE ra
                other.setCenterX(other.getCenterX() - nx * overlap);
                other.setCenterY(other.getCenterY() - ny * overlap);

                // Phản xạ hướng bóng active
                double dotProduct = other.directionX * (-nx) + other.directionY * (-ny);

                if (dotProduct < 0) {
                    other.directionX -= 2 * dotProduct * (-nx);
                    other.directionY -= 2 * dotProduct * (-ny);
                }
            }

            // TRƯỜNG HỢP 3: CẢ HAI DÍNH → Không xử lý (để StickyPaddle tự quản lý vị trí)
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

        if (ballCenterY < paddleTop && ballCenterX >= paddleLeft - getRadius()/Math.sqrt(2) && ballCenterX <= paddleRight + getRadius()/Math.sqrt(2) && directionY > 0) {
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
                double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
                double relativeIntersect = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2.0);
                relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));
                double bounceAngle = relativeIntersect * MAX_ANGLE;

                double baseDirectionX = Math.sin(bounceAngle);
                double baseDirectionY = -Math.cos(bounceAngle);

                double paddleVelocityX = paddle.getVelocityX();
                double transferredMomentum = paddleVelocityX * PADDLE_MOMENTUM_INFLUENCE;

                double baseSpeed = ORIGINAL_SPEED * baseSpeedMultiplier;

                double baseVelX = baseDirectionX * baseSpeed;
                double baseVelY = baseDirectionY * baseSpeed;

                long currentTime = System.currentTimeMillis();

                if (boostStacks < MAX_BOOST_STACKS) {
                    boostStacks++;
                    currentBoost = boostStacks * INSTANT_BOOST_PER_HIT;
                    lastPaddleHitTime = currentTime;
                } else {
                    lastPaddleHitTime = currentTime;
                }

                double finalVelX = baseVelX + transferredMomentum;
                double finalVelY = baseVelY;

                double velocityMagnitude = Math.sqrt(finalVelX * finalVelX + finalVelY * finalVelY);

                double newSpeed = velocityMagnitude + currentBoost;

                if (velocityMagnitude > 0.01) {
                    this.directionX = finalVelX / velocityMagnitude;
                    this.directionY = finalVelY / velocityMagnitude;
                } else {
                    this.directionX = baseDirectionX;
                    this.directionY = baseDirectionY;
                }

                setSpeed(newSpeed);
            }

        } else if (ballCenterX < paddleLeft - getRadius()/Math.sqrt(2) && directionX > 0) {
            setCenterX(paddleLeft - radius - 0.1);
            directionX = -Math.abs(directionX);

        } else if (ballCenterX > paddleRight + getRadius()/Math.sqrt(2) && directionX < 0) {
            setCenterX(paddleRight + radius + 0.1);
            directionX = Math.abs(directionX);

        } else {
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

    @Override
    public void setSpeed(double speed) {
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
            resetBoost();
        }
    }

    public void resetBoost() {
        boostStacks = 0;
        currentBoost = 0.0;
        lastPaddleHitTime = 0;
        updateSpeedWithBoost();
    }

    public int getBoostStacks() {
        return boostStacks;
    }

    public double getCurrentBoost() {
        return currentBoost;
    }

    public void setBaseSpeedMultiplier(double multiplier) {
        this.baseSpeedMultiplier = multiplier;
        updateSpeedWithBoost();
    }

    // ⭐ QUAN TRỌNG: Getter cho baseSpeedMultiplier
    public double getBaseSpeedMultiplier() {
        return baseSpeedMultiplier;
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