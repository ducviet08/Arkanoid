// Arkanoid/view/Renderer.java

package view;

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

    /**
     * Cập nhật danh sách các đối tượng cần vẽ và thông tin điểm số/mạng.
     * Sau đó yêu cầu vẽ lại trên GraphicsContext.
     *
     * @param objects Danh sách các GameObject cần vẽ.
     * @param score   Điểm số hiện tại.
     * @param lives   Số mạng hiện tại.
     */
    public void draw(List<GameObject> objects, int score, int lives) {
        this.gameObjectsToRender = objects;
        this.score = score;
        this.lives = lives;
        renderAll();
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