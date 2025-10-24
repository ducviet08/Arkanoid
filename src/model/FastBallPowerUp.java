// Arkanoid/model/FastBallPowerUp.java
package model;

public class FastBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 1.5; // Tăng tốc bóng lên 1.5 lần
    private Ball gameBall; // Tham chiếu đến quả bóng chính của game

    public FastBallPowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
        this.gameBall = ball;
    }

    // Setter để GameManager có thể thiết lập tham chiếu đến ball
    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle) { // Paddle không thay đổi, chỉ ball thay đổi
        System.out.println("FastBall PowerUp activated!");
        gameBall.setImage("/images/fast_ball.png");
        if (gameBall != null) {
            gameBall.setSpeed(Ball.ORIGINAL_SPEED * SPEED_MULTIPLIER);
        }
    }

    @Override
    public void removeEffect(Paddle paddle) { // Paddle không thay đổi, chỉ ball thay đổi
        System.out.println("FastBall PowerUp deactivated!");
        if (gameBall != null) {
            gameBall.setOriginalSpeed();
            gameBall.setImage("/images/normal_ball.png");
        }
    }
}