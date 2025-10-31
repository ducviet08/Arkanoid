package model;

import static Arkanoid.Main.ballImage;

public class FireBallPowerUp extends PowerUp {
    private Ball ball;

    public FireBallPowerUp(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
        this.ball = ball;
    }

    public void setGameBall(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        ball.setImage("/images/fire_ball.png");
    }

    @Override
    public void removeEffect(Paddle paddle) {
        if (ball != null) {
            ball.setImage(ballImage);
        }
    }
}
