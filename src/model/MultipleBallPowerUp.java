// Arkanoid/model/MultipleBallPowerUp.java
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * MultipleBallPowerUp - Power-up nhân đôi số lượng bóng
 *
 * CÁCH HOẠT ĐỘNG:
 * 1. Khi paddle ăn power-up này, mỗi bóng ACTIVE hiện có sẽ sinh ra 1 bóng mới
 * 2. Bóng mới xuất hiện tại cùng vị trí, với hướng đi khác (góc lệch ±30°)
 * 3. Không có thời gian duration - bóng tồn tại cho đến khi rơi xuống đáy
 * 4. Chỉ mất mạng khi TẤT CẢ bóng rơi xuống đáy
 *
 * NHẬN POWER-UP:
 * - Paddle va chạm với PowerUp
 * - GameManager gọi setBalls() để truyền reference đến danh sách bóng
 * - GameManager gọi applyEffect() để kích hoạt
 */
public class MultipleBallPowerUp extends PowerUp {
    private List<Ball> balls; // Reference đến danh sách bóng trong GameManager

    public MultipleBallPowerUp(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, FALL_SPEED, 0); // duration = 0 (không có thời gian hiệu lực)
    }

    /**
     * Set reference đến danh sách bóng của game
     * GỌI TRƯỚC KHI applyEffect()
     */
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

        // Tạo danh sách bóng mới (tránh ConcurrentModificationException)
        List<Ball> newBalls = new ArrayList<>();

        // Duyệt qua từng bóng active và tạo bóng mới
        for (Ball existingBall : new ArrayList<>(balls)) {
            if (!existingBall.isActive()) {
                continue; // Skip bóng chưa active
            }

            // Tạo 1 bóng mới với góc lệch
            Ball newBall = createMultipliedBall(existingBall);
            newBalls.add(newBall);
        }

        // Thêm tất cả bóng mới vào danh sách chính
        balls.addAll(newBalls);

        System.out.println("🎾 MultipleBall activated!");
        System.out.println("   Created " + newBalls.size() + " new ball(s)");
        System.out.println("   Total balls: " + balls.size());
        System.out.println("   Active balls: " + (activeBallCount + newBalls.size()));
    }

    /**
     * Tạo bóng mới với góc lệch 30° so với bóng gốc
     *
     * LOGIC:
     * - Bóng gốc đi theo direction (dirX, dirY)
     * - Bóng mới sẽ lệch 30° sang trái
     * - Tốc độ, kích thước giống bóng gốc
     */
    private Ball createMultipliedBall(Ball originalBall) {
        // Lấy direction hiện tại của bóng gốc
        double origDirX = originalBall.getDirectionX();
        double origDirY = originalBall.getDirectionY();

        // Tính góc hiện tại (radian)
        double currentAngle = Math.atan2(origDirY, origDirX);

        // Lệch góc 30° (hoặc -30° ngẫu nhiên)
        double angleOffset = Math.toRadians(30);
        if (Math.random() < 0.5) {
            angleOffset = -angleOffset; // 50% lệch trái, 50% lệch phải
        }

        double newAngle = currentAngle + angleOffset;

        // Tính direction mới
        double newDirX = Math.cos(newAngle);
        double newDirY = Math.sin(newAngle);

        // Tạo bóng mới tại vị trí bóng gốc
        Ball newBall = new Ball(
                originalBall.getImagePath(),
                originalBall.getX(),
                originalBall.getY(),
                newDirX,
                newDirY
        );

        // Copy các thuộc tính từ bóng gốc
        newBall.setSpeed(originalBall.getSpeed());
        newBall.setRadius(originalBall.getRadius());
        newBall.setRotationAngle(originalBall.getRotationAngle());
        newBall.setActive(true); // Kích hoạt ngay lập tức

        // Copy boost stacks (nếu có)
        // Bóng mới sẽ không kế thừa boost để tránh quá mạnh
        // newBall.resetBoost(); // (đã reset mặc định khi khởi tạo)

        return newBall;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        // MultipleBall không có effect cần remove
        // Bóng sẽ tự động bị xóa khi rơi xuống đáy (xử lý trong GameManager)
        System.out.println("🎾 MultipleBall: No effect to remove (balls remain active)");
    }

    @Override
    public long getDuration() {
        // Return 0 vì không có thời gian hiệu lực
        // Bóng tồn tại cho đến khi rơi xuống đáy
        return 0;
    }

    /**
     * Đếm số bóng hiện có
     */
    public int getBallCount() {
        return balls != null ? balls.size() : 0;
    }

    /**
     * Đếm số bóng đang active
     */
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