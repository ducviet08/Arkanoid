package Arkanoid;

import controller.GameManager;
import view.Renderer;
import model.GameObject;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Stage primaryStage;
    private Scene menuScene, gameScene, endScene, pauseScene;

    private Renderer renderer;
    private GameManager gameManager;
    private AnimationTimer gameLoop;
    private Set<KeyCode> activeKeys = new HashSet<>();

    private boolean isPaused = false;
    private int currentLevel = 1;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Arkanoid Clone (JavaFX)");
        primaryStage.setResizable(false);

        showMenu();
        primaryStage.show();
    }

    // ------------------ MENU ------------------
    private void showMenu() {
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> startGame());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));

        VBox menuLayout = new VBox(20, startButton, exitButton);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: black;");

        menuScene = new Scene(menuLayout, WIDTH, HEIGHT);
        primaryStage.setScene(menuScene);
    }

    // ------------------ GAME ------------------
    private void startGame() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        renderer = new Renderer(gc);
        gameManager = new GameManager(renderer);

        Pane root = new Pane(canvas);
        gameScene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        // Xử lý phím
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
                return;
            }
            activeKeys.add(event.getCode());
            gameManager.handleInput(event.getCode().ordinal());
        });
        gameScene.setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode());
            gameManager.handleKeyReleased(event.getCode().ordinal());
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT)
                gameManager.getPaddle().stop();
        });

        primaryStage.setScene(gameScene);
        gameManager.startGame();


        // Vòng lặp game
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) return; // Dừng cập nhật khi pause

                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                gameManager.updateGame();

                // Thu thập và vẽ
                List<GameObject> objects = new ArrayList<>();
                objects.add(gameManager.getPaddle());
                objects.add(gameManager.getBall());
                objects.addAll(gameManager.getBricks());
                objects.addAll(gameManager.getPowerUps());
                renderer.draw(objects, gameManager.getScore(), gameManager.getLives());

                // Kiểm tra kết thúc
                if (gameManager.getGameState() == GameManager.GameState.GAME_OVER ||
                        gameManager.getGameState() == GameManager.GameState.LEVEL_COMPLETE) {
                    stop();
                    boolean win = (gameManager.getGameState() == GameManager.GameState.LEVEL_COMPLETE);
                    showEndScreen(gameManager.getScore(), win);
                }
            }
        };

        gameLoop.start();
    }

    // ------------------ PAUSE ------------------
    private void togglePause() {
        if (isPaused) {
            isPaused = false;
            primaryStage.setScene(gameScene);
        } else {
            isPaused = true;
            showPauseScreen();
        }
    }

    private void showPauseScreen() {
        Label pausedLabel = new Label("⏸ Game Paused");
        pausedLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> togglePause());

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            gameLoop.stop();
            showMenu();
        });

        VBox pauseLayout = new VBox(15, pausedLabel, resumeButton, backButton);
        pauseLayout.setAlignment(Pos.CENTER);
        pauseLayout.setStyle("-fx-background-color: black;");

        pauseScene = new Scene(pauseLayout, WIDTH, HEIGHT);
        primaryStage.setScene(pauseScene);
    }

    // ------------------ END GAME ------------------
    private void showEndScreen(int score, boolean win) {
        Label message = new Label(win ? "🎉 LEVEL COMPLETE!" : "💀 GAME OVER!");
        message.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        Label scoreLabel = new Label("Your Score: " + score);
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        Button retryButton = new Button("Retry");
        retryButton.setOnAction(e -> startGame());

        Button backToMenu = new Button("Back to Menu");
        backToMenu.setOnAction(e -> showMenu());

        VBox layout;
        if (win) {
            Button nextLevelButton = new Button("Next Level");
            nextLevelButton.setOnAction(e -> startNextLevel());
            layout = new VBox(15, message, scoreLabel, nextLevelButton, retryButton, backToMenu);
        } else {
            layout = new VBox(15, message, scoreLabel, retryButton, backToMenu);
        }

        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");

        endScene = new Scene(layout, WIDTH, HEIGHT);
        primaryStage.setScene(endScene);
    }

    // ------------------ NEXT LEVEL ------------------
    private void startNextLevel() {
        currentLevel++;
        System.out.println("Starting level " + currentLevel);
        // Ở đây bạn có thể gọi gameManager.loadLevel("level" + currentLevel + ".txt");
        gameManager.startGame();
        startGame();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
