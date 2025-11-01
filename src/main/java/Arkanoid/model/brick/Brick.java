// Arkanoid/Brick.java
package Arkanoid.model.brick;

import Arkanoid.model.base.GameObject;

public abstract class Brick extends GameObject {
    protected int health;

    public Brick(String imagePath, double x, double y, double width, double height, int health) {
        super(imagePath, x, y, width, height);
        this.health = health;
    }

    /**
     * getter function.
     */
    public int getHealth() {
        return health;
    }

//    public String getType() {
//        return type;
//    }

    public void takeHit() {
        health--;
        System.out.println(" Brick hit! Health: " + health);
        if (health > 0) {
            String s[] = super.getPath().split("\\.");
            super.setPath(s[0] + "-1" + ".png");
            super.setImage(s[0]+ "-1" + ".png");
        }
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void takeDestroy() {
        health = 0;
        System.out.println(" Brick destroyed!");
    }

    @Override
    public void update() {
        // Gạch không di chuyển, không có logic update đặc biệt
    }

    @Override
    public void render() {
        // Logic render đồ họa đã được chuyển sang Renderer.draw()
        // Đây chỉ là một placeholder cho debug console
        // System.out.println(type + " Brick at (" + (int)x + ", " + (int)y + "), Health: " + health);
    }
}