// Arkanoid/model/StickyPaddlePowerUp.java
package model;

import static Arkanoid.Main.paddleImage;

public class StickyPaddlePowerUp extends PowerUp {
    private Ball gameBall;
    private boolean stuck = false;
    private Paddle currentPaddle;
    private double offSetX = 0;

    public StickyPaddlePowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
        this.gameBall = ball;
    }

    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        System.out.println("StickyPaddle PowerUp activated!");
        paddle.setImage("/images/paddle2.png");
        paddle.setSticky(true);
        this.currentPaddle = paddle;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        paddle.setImage(paddleImage);
        paddle.setSticky(false);
        System.out.println("StickyPaddle PowerUp deactivated!");
    }

    /**
     * Xử lý bóng dính khi va chạm với paddle
     * CẬP NHẬT: Sử dụng getCenterX() thay vì getX() cho bóng tròn
     */
    public void onBallHitPaddle(Ball ball, Paddle paddle) {
        if (paddle.isSticky() && !stuck) {
            stuck = true;
            this.currentPaddle = paddle;
            this.gameBall = ball;

            // Dừng bóng
            gameBall.setActive(false);

            // Tính offset từ tâm bóng đến paddle
            this.offSetX = gameBall.getCenterX() - paddle.getX();

            // Giới hạn biên paddle khi bóng dính ở mép
            // Kiểm tra mép trái
            if (gameBall.getCenterX() - gameBall.getRadius() < currentPaddle.getX()) {
                double overlap = currentPaddle.getX() - (gameBall.getCenterX() - gameBall.getRadius());
                currentPaddle.setLeftBoder(currentPaddle.getLeftBoder() + overlap);
            }

            // Kiểm tra mép phải
            if (gameBall.getCenterX() + gameBall.getRadius() > currentPaddle.getX() + currentPaddle.getWidth()) {
                double overlap = (gameBall.getCenterX() + gameBall.getRadius()) -
                        (currentPaddle.getX() + currentPaddle.getWidth());
                currentPaddle.setRightBoder(currentPaddle.getRightBoder() - overlap);
            }
        }
    }

    /**
     * Thả bóng ra từ paddle
     * CẬP NHẬT: Sử dụng getCenterX() và tính toán góc bật chính xác
     */
    public void releaseBall(Ball gameBall, Paddle paddle) {
        if (stuck && gameBall != null) {
            this.gameBall = gameBall;
            this.currentPaddle = paddle;

            // Cho phép bóng di chuyển
            stuck = false;
            gameBall.setActive(true);

            // Tính góc bật dựa trên vị trí dính
            double ballCenterX = gameBall.getCenterX();
            double paddleCenterX = currentPaddle.getX() + currentPaddle.getWidth() / 2.0;
            double relativeIntersect = (ballCenterX - paddleCenterX) / (currentPaddle.getWidth() / 2.0);

            // Giới hạn [-1, 1]
            relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));

            // Tính góc bật
            double bounceAngle = relativeIntersect * Ball.MAX_ANGLE;

            // Set hướng bóng
            gameBall.setDirectionX(Math.sin(bounceAngle));
            gameBall.setDirectionY(-Math.cos(bounceAngle));

            // Reset biên paddle
            currentPaddle.setRightBoder(800);
            currentPaddle.setLeftBoder(0);
        }
    }

    public boolean isStuck() {
        return stuck;
    }

    public double getOffSetX() {
        return offSetX;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    public void setOffSetX(double offSetX) {
        this.offSetX = offSetX;
    }
}