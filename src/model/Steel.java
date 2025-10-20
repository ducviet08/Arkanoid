// Arkanoid/model/Steel.java
package model;

public class Steel extends GameObject {

    public Steel(double x, double y, double width, double height) {
        super(x, y, width, height); // Gạch không phá đươc.

    }

    public void takeHit() {
        System.out.println("Steel cannot be destroyed");
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }
}
