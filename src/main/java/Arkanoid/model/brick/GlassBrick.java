package Arkanoid.model.brick;

public class GlassBrick extends Brick {

    public GlassBrick(String imagePath, double x, double y, double width, double height) {
        super(imagePath, x, y, width, height, 2);
    }

    public GlassBrick(String imagePath, double x, double y, double width, double height, int health) {
        super(imagePath, x, y, width, height, health);
    }

    @Override
    public void update() {

    }
}
