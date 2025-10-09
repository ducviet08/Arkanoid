package model;

public class InvisibleBrick extends Brick{
    private boolean visible;

    public InvisibleBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 2, "Invisible", true);
        this.visible = false;
    }

    @Override
    public void takeHit() {
        if (!visible) {
            visible = true;
        }
        health--;
    }

    public boolean getVisible() {
        return visible;
    }
}
