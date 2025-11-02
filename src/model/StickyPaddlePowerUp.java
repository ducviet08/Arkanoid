//
//
//// Arkanoid/model/FastBallPowerUp.java
//package model;
//
//import static Arkanoid.Main.paddleImage;
//
//public class StickyPaddlePowerUp extends PowerUp {
//    private Ball gameBall; // Tham chiếu đến quả bóng chính của game
//    private boolean stuck = false;  // bóng đang dính trên paddle
//    private Paddle currentPaddle;   // Paddle hiện tại
//
//    private double relativeOffset = 0.5;
//
//    public StickyPaddlePowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
//        super(imagePath, x, y, width, height, FALL_SPEED, duration);
//        this.gameBall = ball;
//    }
//
//    // Setter để GameManager có thể thiết lập tham chiếu đến ball
//    public void setGameBall(Ball gameBall) {
//        this.gameBall = gameBall;
//    }
//
//    @Override
//    public void applyEffect(Paddle paddle) {
//        System.out.println("StickyPaddle PowerUp activated!");
//        paddle.setImage("/images/paddle1.png");
//        paddle.setSticky(true);
//        this.currentPaddle = paddle;
//    }
//
//    @Override
//    public void removeEffect(Paddle paddle) {
//        paddle.setImage(paddleImage);
//        System.out.println("ExpandPaddle PowerUp deactivated!");
//    }
//
//    // Xử lý bóng dính khi va chạm
//    public void onBallHitPaddle(Ball ball, Paddle paddle) {
//        if (paddle.isSticky() && !stuck) {
//            stuck = true;
//
//            // Tham chiếu đến ball và paddle hiện tại
//            this.currentPaddle = paddle;
//            this.gameBall = ball;
//
//            gameBall.setActive(false);  // Ball ngừng di chuyển
//
//            // Vị trí của bóng và Paddle
//            double absoluteOffset = gameBall.getX() - paddle.getX();
//
//            // Tính toán tỉ lệ offset
//            this.relativeOffset = absoluteOffset / paddle.getWidth();
//
//            // Đảm bảo tỉ lệ luôn nằm trong khoảng hợp lệ [0, 1]
//            // (Phòng trường hợp bóng va chạm ở rìa ngoài)
//            this.relativeOffset = Math.max(0, Math.min(1, this.relativeOffset));
//        }
//    }
//
//    // Ném bóng ra
//    public void releaseBall(Ball gameBall, Paddle paddle) {
//        if(stuck && gameBall != null) {
//            // Tham chiếu đến đến bóng và paddle hiện tại
//            this.gameBall = gameBall;
//            this.currentPaddle = paddle;
//
//            // cho bóng di chuyển
//            stuck = false;
//            //this.setStuck(stuck);
//            gameBall.setActive(true);
//
//            // Điều chỉnh hướng bóng
//            double ballCenterX = gameBall.x + gameBall.width / 2;
//            double paddleCenterX = currentPaddle.getX() + currentPaddle.getWidth() / 2.0;
//            double relativeIntersect = (ballCenterX - paddleCenterX) / (currentPaddle.getWidth() / 2.0);
//
//            // Giới hạn lại giá trị [-1, 1]
//            relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));
//
//            // Góc bật (0 = giữa, ±MAX_ANGLE = hai mép)
//            double bounceAngle = relativeIntersect * Ball.MAX_ANGLE;
//
//            // Tính lại hướng bóng
//            gameBall.setDirectionX(Math.sin(bounceAngle));
//            gameBall.setDirectionY(-Math.cos(bounceAngle));
//
//            currentPaddle.setRightBoder(800);
//            currentPaddle.setLeftBoder(0);
//        }
//    }
//
//    public boolean isStuck() {
//        return stuck;
//    }
//
//
//    public void setStuck(boolean stuck) {
//        this.stuck = stuck;
//    }
//
//    public double getRelativeOffset() {
//        return relativeOffset;
//    }
//
//    public void setRelativeOffset(double relativeOffset) {
//        this.relativeOffset = relativeOffset;
//    }
//
//}

// Arkanoid/model/StickyPaddlePowerUp.java
package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import static Arkanoid.Main.paddleImage;

/**
 * StickyPaddlePowerUp - Paddle dính bóng
 *
 * CẢI TIẾN MỚI:
 * ✅ Hỗ trợ NHIỀU bóng dính cùng lúc (Map<Ball, Offset>)
 * ✅ Ném TẤT CẢ bóng cùng lúc khi nhấn ENTER
 * ✅ Quản lý offset riêng cho từng bóng
 * ✅ Các method mới: isStuckBall(), releaseAllBalls(), getStuckBallCount()
 */
public class StickyPaddlePowerUp extends PowerUp {
    private Paddle currentPaddle;

    // ===== QUẢN LÝ NHIỀU BÓNG DÍNH =====
    // Map: Ball -> RelativeOffset (vị trí tương đối trên paddle [0-1])
    private Map<Ball, Double> stuckBalls = new HashMap<>();

    public StickyPaddlePowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
        // ball parameter không còn cần thiết nhưng giữ để tương thích
    }

    public void setGameBall(Ball gameBall) {
        // Không cần nữa vì hỗ trợ nhiều bóng
        // Giữ lại method này để tương thích với code cũ
    }

    @Override
    public void applyEffect(Paddle paddle) {
        System.out.println("🎯 StickyPaddle PowerUp activated!");
        paddle.setImage("/images/paddle1.png");
        paddle.setSticky(true);
        this.currentPaddle = paddle;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        paddle.setImage(paddleImage);
        paddle.setSticky(false);

        // Giải phóng tất cả bóng đang dính
        releaseAllBalls(paddle);

        System.out.println("🎯 StickyPaddle PowerUp deactivated!");
    }

    /**
     * ⭐ Xử lý bóng dính khi va chạm với paddle
     * Được gọi từ Ball.handlePaddleCollision()
     */
    public void onBallHitPaddle(Ball ball, Paddle paddle) {
        if (paddle.isSticky() && !stuckBalls.containsKey(ball)) {
            // === Dính bóng mới ===

            this.currentPaddle = paddle;
            ball.setActive(false);

            // Tính vị trí tương đối của bóng trên paddle
            double absoluteOffset = ball.getX() - paddle.getX();
            double relativeOffset = absoluteOffset / paddle.getWidth();

            // Giới hạn [0, 1] (phòng trường hợp bóng va chạm ở rìa ngoài)

            // Lưu bóng và offset
            stuckBalls.put(ball, relativeOffset);

            // Điều chỉnh biên paddle nếu bóng nằm ngoài (để đảm bảo bóng luôn ở trong paddle)
            if (absoluteOffset < 0) {
                currentPaddle.setLeftBoder(currentPaddle.getLeftBoder() + currentPaddle.getX() - ball.getX());
            }

            if (ball.getX() + ball.getWidth() - currentPaddle.getX() - currentPaddle.getWidth() > 0) {
                currentPaddle.setRightBoder(currentPaddle.getRightBoder() -
                        (ball.getX() + ball.getWidth() - currentPaddle.getX() - currentPaddle.getWidth()));
            }

            System.out.println("🎯 Ball stuck! Total stuck balls: " + stuckBalls.size());
        }
    }

    /**
     * ⭐ NÉM TẤT CẢ BÓNG ĐANG DÍNH
     * Được gọi khi nhấn ENTER trong GameManager.handleInput()
     */
    public void releaseAllBalls(Paddle paddle) {
        if (stuckBalls.isEmpty()) {
            return;
        }

        System.out.println("🚀 Releasing " + stuckBalls.size() + " ball(s)!");

        Iterator<Map.Entry<Ball, Double>> iterator = stuckBalls.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Ball, Double> entry = iterator.next();
            Ball ball = entry.getKey();

            // Kích hoạt bóng
            ball.setActive(true);

            // Tính góc bật dựa trên vị trí tương đối trên paddle
            double ballCenterX = ball.getX() + ball.getWidth() / 2;
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
            double relativeIntersect = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2.0);

            // Giới hạn [-1, 1]
            relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));

            // Góc bật (từ -75° đến +75°)
            double bounceAngle = relativeIntersect * Ball.MAX_ANGLE;

            // Cập nhật hướng bóng
            ball.setDirectionX(Math.sin(bounceAngle));
            ball.setDirectionY(-Math.cos(bounceAngle));

            iterator.remove();
        }

        // Reset biên paddle về mặc định
        paddle.setRightBoder(800);
        paddle.setLeftBoder(0);

        System.out.println("✅ All balls released!");
    }

    /**
     * Ném một bóng cụ thể (dùng cho tương thích với code cũ)
     * Hiện tại không còn dùng vì ném tất cả cùng lúc
     */
    public void releaseBall(Ball ball, Paddle paddle) {
        if (stuckBalls.containsKey(ball)) {
            ball.setActive(true);

            double ballCenterX = ball.getX() + ball.getWidth() / 2;
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
            double relativeIntersect = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2.0);

            relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));
            double bounceAngle = relativeIntersect * Ball.MAX_ANGLE;

            ball.setDirectionX(Math.sin(bounceAngle));
            ball.setDirectionY(-Math.cos(bounceAngle));

            stuckBalls.remove(ball);

            // Nếu không còn bóng nào dính, reset biên paddle
            if (stuckBalls.isEmpty()) {
                paddle.setRightBoder(800);
                paddle.setLeftBoder(0);
            }

            System.out.println("🚀 Ball released! Remaining stuck: " + stuckBalls.size());
        }
    }

    /**
     * ⭐ Kiểm tra xem bóng CỤ THỂ có đang dính không
     * Được gọi từ Ball.move()
     */
    public boolean isStuckBall(Ball ball) {
        return stuckBalls.containsKey(ball);
    }

    /**
     * ⭐ Kiểm tra có bóng nào đang dính không
     * Được gọi từ GameManager để kiểm tra trạng thái
     */
    public boolean isStuck() {
        return !stuckBalls.isEmpty();
    }

    /**
     * ⭐ Lấy offset tương đối của một bóng cụ thể
     * Được gọi từ Ball.move() để cập nhật vị trí khi paddle di chuyển
     */
    public double getRelativeOffset(Ball ball) {
        return stuckBalls.getOrDefault(ball, 0.5); // Default giữa paddle nếu không tìm thấy
    }

    /**
     * ⭐ Số lượng bóng đang dính
     */
    public int getStuckBallCount() {
        return stuckBalls.size();
    }

    /**
     * ⭐ Xóa bóng khỏi danh sách dính
     * Được gọi từ GameManager khi bóng bị remove khỏi game (rơi xuống đáy)
     */
    public void removeBall(Ball ball) {
        stuckBalls.remove(ball);

        // Nếu không còn bóng nào dính, reset biên paddle
        if (stuckBalls.isEmpty() && currentPaddle != null) {
            currentPaddle.setRightBoder(800);
            currentPaddle.setLeftBoder(0);
        }
    }

    /**
     * @deprecated Dùng isStuckBall(Ball) thay thế
     */
    public void setStuck(boolean stuck) {
        // Không còn dùng - giữ lại để tương thích
    }

    /**
     * @deprecated Dùng getRelativeOffset(Ball) thay thế
     */
    public double getRelativeOffset() {
        // Trả về offset của bóng đầu tiên (nếu có)
        if (stuckBalls.isEmpty()) return 0.5;
        return stuckBalls.values().iterator().next();
    }

    /**
     * @deprecated Không còn sử dụng vì hỗ trợ nhiều bóng
     */
    public void setRelativeOffset(double relativeOffset) {
        // Không còn dùng
    }
}