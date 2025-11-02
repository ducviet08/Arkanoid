// Arkanoid/model/MultipleBallPowerUp.java
package model;

import java.util.ArrayList;
import java.util.List;

import static Arkanoid.Main.ballImage;

/**
 * MultipleBallPowerUp - Power-up nhân đôi số lượng bóng
 *
 * CẢI TIẾN MỚI:
 * ✅ Giới hạn tối đa 10 bóng (MAX_BALLS)
 * ✅ Kiểm tra trước khi tạo bóng mới
 * ✅ Cảnh báo khi đạt giới hạn
 * ✅ Copy baseSpeedMultiplier từ bóng gốc
 */
public class MultipleBallPowerUp extends PowerUp {
    private List<Ball> balls;

    // ===== GIỚI HẠN SỐ LƯỢNG BÓNG =====
    public static final int MAX_BALLS = 10; // ⚠️ Có thể thay đổi giá trị này

    public MultipleBallPowerUp(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, FALL_SPEED, 0);
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        if (balls == null || balls.isEmpty()) {
            System.out.println("⚠️ MultipleBall: No balls to multiply!");
            return;
        }

        // Đếm số bóng active
        int activeBallCount = 0;
        for (Ball ball : balls) {
            if (ball.isActive()) {
                activeBallCount++;
            }
        }

        if (activeBallCount == 0) {
            System.out.println("⚠️ MultipleBall: No active balls to multiply!");
            return;
        }

        // ===== KIỂM TRA GIỚI HẠN =====
        if (balls.size() >= MAX_BALLS) {
            System.out.println("⚠️ MultipleBall: Maximum ball limit reached (" + MAX_BALLS + ")");
            System.out.println("   Cannot create more balls!");
            return;
        }

        int totalBallsAfterMultiply = balls.size() + activeBallCount;

        if (totalBallsAfterMultiply > MAX_BALLS) {
            // Chỉ tạo đủ số bóng để đạt giới hạn
            int ballsToCreate = MAX_BALLS - balls.size();
            System.out.println("⚠️ MultipleBall: Limiting to " + ballsToCreate + " new balls (max: " + MAX_BALLS + ")");

            List<Ball> newBalls = new ArrayList<>();
            List<Ball> activeBalls = new ArrayList<>();

            for (Ball ball : balls) {
                if (ball.isActive()) {
                    activeBalls.add(ball);
                }
            }

            // Tạo bóng mới cho một số bóng active (không phải tất cả)
            for (int i = 0; i < Math.min(ballsToCreate, activeBalls.size()); i++) {
                Ball existingBall = activeBalls.get(i);
                Ball newBall = createMultipliedBall(existingBall);
                newBalls.add(newBall);
            }

            balls.addAll(newBalls);

            System.out.println("🎾 MultipleBall activated (LIMITED)!");
            System.out.println("   Created " + newBalls.size() + " new ball(s)");
            System.out.println("   Total balls: " + balls.size() + " / " + MAX_BALLS);
            return;
        }

        // ===== TẠO BÓNG MỚI (FULL) =====
        List<Ball> newBalls = new ArrayList<>();

        for (Ball existingBall : new ArrayList<>(balls)) {
            if (!existingBall.isActive()) {
                continue;
            }

            Ball newBall = createMultipliedBall(existingBall);
            newBalls.add(newBall);
        }

        balls.addAll(newBalls);

        System.out.println("🎾 MultipleBall activated!");
        System.out.println("   Created " + newBalls.size() + " new ball(s)");
        System.out.println("   Total balls: " + balls.size() + " / " + MAX_BALLS);
        System.out.println("   Active balls: " + (activeBallCount + newBalls.size()));
    }

    /**
     * Tạo bóng mới với góc lệch 30° so với bóng gốc
     */
    private Ball createMultipliedBall(Ball originalBall) {
        double origDirX = originalBall.getDirectionX();
        double origDirY = originalBall.getDirectionY();

        double currentAngle = Math.atan2(origDirY, origDirX);

        double angleOffset = Math.toRadians(30);
        if (Math.random() < 0.5) {
            angleOffset = -angleOffset;
        }

        double newAngle = currentAngle + angleOffset;

        double newDirX = Math.cos(newAngle);
        double newDirY = Math.sin(newAngle);

        Ball newBall = new Ball(
                ballImage,
                originalBall.getX(),
                originalBall.getY(),
                newDirX,
                newDirY
        );

        // Copy tất cả thuộc tính từ bóng gốc
        newBall.setSpeed(originalBall.getSpeed());
        newBall.setRadius(originalBall.getRadius());
        newBall.setRotationAngle(originalBall.getRotationAngle());
        newBall.setActive(true);

        // ⭐ QUAN TRỌNG: Copy baseSpeedMultiplier để kế thừa PowerUp
        newBall.setBaseSpeedMultiplier(originalBall.getBaseSpeedMultiplier());

        return newBall;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        System.out.println("🎾 MultipleBall: No effect to remove (balls remain active)");
    }

    @Override
    public long getDuration() {
        return 0;
    }

    public int getBallCount() {
        return balls != null ? balls.size() : 0;
    }

    public int getActiveBallCount() {
        if (balls == null) return 0;

        int count = 0;
        for (Ball ball : balls) {
            if (ball.isActive()) {
                count++;
            }
        }
        return count;
    }
}