package model;

import java.util.List;

public class TeleportBrick extends Brick {
    private int teleportsLeft;
    private static final double BRICKWIDTH = 80;
    private static final double BRICKHEIGHT = 25;
    private static final double GAP = 5;
    private List<Brick> bricks;

    public TeleportBrick(String imagePart, double x, double y, double width, double height, List<Brick> bricks) {
        super(imagePart, x, y, width, height, 1);
        this.teleportsLeft = 3;
        this.bricks = bricks;
    }

    public TeleportBrick(String imagePart, double x, double y, double width, double height, int health, List<Brick> bricks) {
        super(imagePart, x, y, width, height, health);
        this.teleportsLeft = 3;
        this.bricks = bricks;
    }

    @Override
    public void takeHit() {
        if (teleportsLeft > 0) {
            teleportsLeft--;
            this.teleport();
            System.out.println("Teleported!");
        } else {
            health--;
            System.out.println(" Brick hit! Health: " + health);
        }
    }

    private void teleport() {
        int attempt = 0;

        double totalWidth = 8 * BRICKWIDTH + 7 * GAP;
        double marginLeft = (800 - totalWidth) / 2;
        double marginTop = 50;

        while (attempt < 50) {
            int col = (int)(Math.random() * 8);
            int row = (int)(Math.random() * 7);
            double newX = marginLeft + col * (BRICKWIDTH + GAP);
            double newY = marginTop + row * (BRICKHEIGHT + GAP);

            if (!isOverlapping(newX, newY)) {
                this.setX(newX);
                this.setY(newY);
                System.out.println("Teleported to: (" + newX + ", " +newY + ")");
                return;
            }

            attempt ++;
        }
    }

    private boolean isOverlapping(double newX, double newY) {

        for (Brick brick : bricks) {
            if (brick.equals(this) || brick.isDestroyed()) {
                continue;
            }
            
            if (Math.abs(newX - brick.getX()) < 0.01 && Math.abs(newY - brick.getY()) < 0.01) {
                return true;
            }
        }

        return false;
    }
}
