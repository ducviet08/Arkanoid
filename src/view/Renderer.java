// Arkanoid/view/Renderer.java

package Arkanoid.view;

import model.GameObject;
import model.Ball;
import model.Paddle;
import model.NormalBrick;
import model.StrongBrick;
import model.ExpandPaddlePowerUp;
import model.FastBallPowerUp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ArrayList;

/**
 * Lớp Renderer chịu trách nhiệm vẽ các đối tượng game lên màn hình Canvas của JavaFX.
 */
public class Renderer {

    private GraphicsContext gc;
    private List<GameObject> gameObjectsToRender = new ArrayList<>();
    private int score;
    private int lives;

    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }

    public void draw(List<GameObject> objects, int score, int lives) {
    }

    /**
     * Thực hiện vẽ tất cả các đối tượng lên GraphicsContext.
     */
    private void renderAll() {
    }

    public void drawScoreAndLives(int score, int lives) {
        // Logic này có thể được loại bỏ hoặc chỉ dùng cho console log
        // System.out.println("Score: " + score + ", Lives: " + lives);
    }
}