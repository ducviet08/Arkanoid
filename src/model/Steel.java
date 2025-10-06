// Arkanoid/model/Steel.java
package model;

public class Steel extends Brick {

    public Steel(double x, double y, double width, double height) {
        super(x, y, width, height, 1, "Steel", false); // Gạch không phá đươc.
    }

    @Override
    public void takeHit() {
        System.out.println("Steel cannot be destroyed");
    }
}
