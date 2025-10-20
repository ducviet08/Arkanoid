// Arkanoid/view/Renderer.java

package view;

import model.GameObject;
import model.Ball;
import model.Paddle;
import model.NormalBrick;
import model.StrongBrick;
import model.Steel;
import model.InvisibleBrick;
import model.ExpandPaddlePowerUp;
import model.FastBallPowerUp;
import model.ExtraLifePowerUp;

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
        // Xóa toàn bộ Canvas trước khi vẽ lại
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (GameObject obj : gameObjectsToRender) {
            // Đây là một cách đơn giản, trong thực tế bạn có thể cần kiểm tra loại đối tượng
            // để vẽ hình dạng hoặc ảnh tương ứng
            if (obj instanceof Paddle) {
                gc.setFill(Color.BLUE);
                gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            } else if (obj instanceof Ball) {
                gc.setFill(Color.RED);
                gc.fillOval(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            } else if (obj instanceof NormalBrick) {
                gc.setFill(Color.GREEN);
                gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
                gc.setStroke(Color.DARKGRAY);
                gc.setLineWidth(1);
                gc.strokeRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            } else if (obj instanceof StrongBrick) {
                gc.setFill(Color.GRAY);
                gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
                gc.setStroke(Color.DARKGRAY);
                gc.setLineWidth(1);
                gc.strokeRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            }  else if (obj instanceof Steel) {
                gc.setFill(Color.RED);
                gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
                gc.setStroke(Color.DARKGRAY);
                gc.setLineWidth(1);
                gc.strokeRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            } else if (obj instanceof InvisibleBrick) {
                if (!((InvisibleBrick) obj).getVisible()) {
                    gc.setFill(Color.color(0, 0, 0, 0));
                    gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
                    gc.setStroke(Color.color(0, 0, 0, 0));
                    gc.setLineWidth(1);
                    gc.strokeRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
                    gc.setStroke(Color.DARKGRAY);
                    gc.setLineWidth(1);
                    gc.strokeRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
                }
            }else if (obj instanceof ExpandPaddlePowerUp) {
                gc.setFill(Color.YELLOW);
                gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            } else if (obj instanceof FastBallPowerUp) {
                gc.setFill(Color.CYAN);
                gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            } else if (obj instanceof ExtraLifePowerUp) {
                gc.setFill(Color.PINK);
                gc.fillRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            }
        }

        // Vẽ điểm số và mạng
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 18));
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Lives: " + lives, gc.getCanvas().getWidth() - 100, 20);
    }

    // drawScoreAndLives() không còn cần thiết vì đã có draw(List, int, int)
    // Nếu bạn muốn hiển thị debug log cho điểm và mạng, có thể giữ lại
    public void drawScoreAndLives(int score, int lives) {
        // Logic này có thể được loại bỏ hoặc chỉ dùng cho console log
        // System.out.println("Score: " + score + ", Lives: " + lives);
    }
}