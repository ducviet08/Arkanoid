// Arkanoid/model/ExtraLifePowerUp
package model;

public class ExtraLifePowerUp extends PowerUp {

    public ExtraLifePowerUp(double x, double y, double width, double height) {
        super(x, y, width, height, FALL_SPEED, 0);
    }

    @Override
    public void applyEffect(Paddle paddle) {

    }

    @Override
    public void removeEffect(Paddle paddle) {

    }
}
