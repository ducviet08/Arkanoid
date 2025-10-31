// Arkanoid/model/GameObject.java
package model;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GameObject {
    protected double x, y, width, height;
    protected Image image;
    protected String path;

    public GameObject(String imagePath, double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        image = new Image(imagePath);
        path = imagePath;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String path) {
        image = new Image(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // Phương thức kiểm tra va chạm giữa hai GameObject
    public boolean checkCollision(GameObject other) {
        return this.x < other.x + other.width &&
                this.x + this.width > other.x &&
                this.y < other.y + other.height &&
                this.y + this.height > other.y;
    }

    public abstract void update();
    public abstract void render(); // Giữ lại cho mục đích debug console nếu cần
}