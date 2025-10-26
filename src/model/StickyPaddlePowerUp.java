package model;

import static Arkanoid.Main.ballImage;
import static Arkanoid.Main.paddleImage;

public class StickyPaddlePowerUp extends PowerUp {
    private Ball gameBall;

    public StickyPaddlePowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
        this.gameBall = ball;
    }
    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }
    @Override
    public void applyEffect(Paddle paddle) {
        paddle.setImage("/images/paddle2.png");
    }
    @Override
    public void removeEffect(Paddle paddle) {
        paddle.setImage(paddleImage);
        System.out.println("ExpandPaddle PowerUp deactivated!");
    }
}
