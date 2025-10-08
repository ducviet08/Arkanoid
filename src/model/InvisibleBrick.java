package Arkanoid.model;

public class InvisibleBrick extends Brick{
    private boolean visible;

    public InvisibleBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 2, "Invisible", true);
        this.Visible = false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean onHit() {
        if (!visible) {
            visible = true;
        }
    }
}
