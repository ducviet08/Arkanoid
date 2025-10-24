// Arkanoid/Main.java
package Arkanoid;

import controller.GameManager;
import controller.SaveLoadGame;
import view.Renderer;
import model.GameObject;
import model.Ball;
import model.Paddle;
import model.NormalBrick;
import model.StrongBrick;
import model.ExpandPaddlePowerUp;
import model.FastBallPowerUp;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Renderer renderer;
    private GameManager gameManager;
    private AnimationTimer gameLoopTimer;

    // Sử dụng Set để theo dõi các phím đang được nhấn
    private Set<KeyCode> activeKeys = new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        //Cửa sổ chính của canvas
        primaryStage.setTitle("Arkanoid Clone (JavaFX)");
        primaryStage.setResizable(false);

        //Mặt phẳng canvas
        Canvas gameCanvas = new Canvas(WIDTH, HEIGHT);
        //đối tượng để vẽ
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        renderer = new Renderer(gc);
        gameManager = new GameManager(renderer);

        //thành phần giao diện bên trong
        Pane root = new Pane(gameCanvas);
        //bối cảnh chính cảu game
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);

        // Xử lý sự kiện bàn phím
        scene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());
            gameManager.handleInput(event.getCode().ordinal());
        });

        scene.setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode());
            gameManager.handleKeyReleased(event.getCode().ordinal());
            // Đảm bảo dừng paddle khi nhả phím
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                gameManager.getPaddle().stop();
            }
        });

        // Xử lý sự kiện đóng cửa sổ - Lưu game trước khi thoát
        primaryStage.setOnCloseRequest(event -> {
            // Dừng game loop trước
            if (gameLoopTimer != null) {
                gameLoopTimer.stop();
            }

            // Lưu game
            try {
                SaveLoadGame.saveGame(gameManager);
                System.out.println("Game đã được lưu vào save.txt");
            } catch (Exception e) {
                System.err.println("Lỗi khi lưu game: " + e.getMessage());
                e.printStackTrace();
            }
        });

        //hiển thị cửa sổ
        primaryStage.setScene(scene);
        primaryStage.show();

        //bắt đầu game
        try {
            gameManager.startGame();
            //SaveLoadGame.loadGame(gameManager);
            System.out.println("Đã load game từ save.txt thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi load game từ save.txt: " + e.getMessage());
            System.out.println("Bắt đầu game mới từ level1.txt...");
            gameManager.startGame(); // Fallback về cách cũ nếu load thất bại
        }

        // Vòng lặp game sử dụng AnimationTimer
        gameLoopTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 1. Cập nhật logic game
                gameManager.updateGame();

                // 2. Thu thập tất cả các GameObject cần vẽ từ GameManager
                List<GameObject> objectsToRender = new ArrayList<>();
                objectsToRender.add(gameManager.getPaddle());
                objectsToRender.add(gameManager.getBall());
                objectsToRender.addAll(gameManager.getBricks());
                objectsToRender.addAll(gameManager.getSteels());
                objectsToRender.addAll(gameManager.getPowerUps());

                // 3. Yêu cầu Renderer vẽ lại màn hình
                renderer.draw(objectsToRender, gameManager.getScore(), gameManager.getLives());

                // Nếu game kết thúc, dừng timer
                if (gameManager.getGameState() == GameManager.GameState.GAME_OVER ||
                        gameManager.getGameState() == GameManager.GameState.LEVEL_COMPLETE) {
                    this.stop();

                    // Lưu game trước khi hiển thị thông báo kết thúc
                    try {
                        SaveLoadGame.saveGame(gameManager);
                        System.out.println("Game đã được lưu vào save.txt");
                    } catch (Exception e) {
                        System.err.println("Lỗi khi lưu game: " + e.getMessage());
                    }

                    String message = (gameManager.getGameState() == GameManager.GameState.GAME_OVER) ?
                            "GAME OVER! Score: " + gameManager.getScore() :
                            "LEVEL COMPLETE! Score: " + gameManager.getScore();
                    // Trong JavaFX, bạn có thể dùng Alert hoặc Stage mới để hiển thị kết quả
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Ended");
                    alert.setHeaderText(null);
                    alert.setContentText(message + "\n\nGame đã được lưu!");
                    alert.showAndWait();
                    primaryStage.close();
                }
            }
        };
        gameLoopTimer.start();
    }

    public static void main(String[] args) {
        //gọi javaFX để runtime
        launch(args);
    }
}