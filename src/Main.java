package Arkanoid;

import controller.GameManager;
import controller.SaveLoadGame;
import controller.SoundManager;
import javafx.scene.image.Image;
import view.*;
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

    public boolean Continue = false;
    private SaveLoadGame saveLoadGame = new SaveLoadGame();
    public static String ballImage;
    public static String paddleImage;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Stage primaryStage;
    private Scene menuScene, gameScene, endScene, pauseScene;

    private Renderer renderer;
    private GameManager gameManager;
    private AnimationTimer gameLoop;
    private Set<KeyCode> activeKeys = new HashSet<>();

    private boolean isPaused = false;
    public static int currentLevel = 1;

    private StartScreen startScreen = new StartScreen();
    private EndScreen endScreen = new EndScreen();
    private PauseScreen pauseScreen = new PauseScreen();

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
        Scene menuSceneFromStartScreen = startScreen.getScene(primaryStage, this);
        //startScreen.getStartGameButton().setOnAction(e -> startGame());
        //startScreen.getExitButton().setOnAction(e -> System.exit(0));
        primaryStage.setScene(menuSceneFromStartScreen);
    }


    // ------------------ GAME ------------------
    public void startGame() {
        SoundManager.playSound(SoundManager.SOUND_GAME_START);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        renderer = new Renderer(gc);
        gameManager = new GameManager(renderer);
        Pane root = new Pane(canvas);
        gameScene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        gameManager.initializeGame();
        primaryStage.setScene(gameScene);
        if (Continue) {
            saveLoadGame.loadGame(gameManager);
            System.out.println(gameManager.getLives());
        } else {
            gameManager.loadLevel("level" + currentLevel + ".txt");
            gameManager.getBall().setImage(ballImage);
            gameManager.getPaddle().setImage(paddleImage);
        }

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
                    saveLoadGame.saveGame(gameManager);
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
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            isPaused = false;
            primaryStage.setScene(gameScene); // Quay lại scene game
            gameLoop.start(); // Khởi động lại game loop
        } else {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            isPaused = true;
            gameLoop.stop(); // Dừng game loop khi pause
            showPauseScreen();
        }
    }

    private void showPauseScreen() {
        Scene pauseScene = pauseScreen.getScene(primaryStage, WIDTH, HEIGHT);

        saveLoadGame.saveGame(gameManager);
        pauseScreen.getContinueButton().setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            togglePause();
        });
        pauseScreen.getExitToMenuButton().setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            gameLoop.stop();
            isPaused = false; // Đảm bảo trạng thái không bị pause khi về menu
            currentLevel = 1;
            showMenu();
        });

        primaryStage.setScene(pauseScene);
    }

    // ------------------ END GAME ------------------
    private void showEndScreen(int score, boolean win) {
        endScreen.setMessage(win ? "🎉 LEVEL COMPLETE!" : "💀 GAME OVER!");
        endScreen.setScore(score);

        Scene endScene = endScreen.getScene(primaryStage, WIDTH, HEIGHT, win);

        endScreen.getRestartButton().setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            currentLevel = 1;
            showMenu();
        });

        endScreen.getExitToMenuButton().setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            currentLevel = 1;
            showMenu();
        });

        // Chỉ gắn hành động cho Next Level khi có nút này
        if (win) {
            SaveLoadGame.saveGame(gameManager);
            endScreen.getNextLevelButton().setOnAction(e ->
            {
                SoundManager.playSound(SoundManager.SOUND_WIN_GAME);
                startNextLevel();
            });
        }

        primaryStage.setScene(endScene);
    }

    // ------------------ NEXT LEVEL ------------------
    private void startNextLevel() {
        SoundManager.playSound(SoundManager.SOUND_GAME_START);
        currentLevel++;
        System.out.println("Starting level " + currentLevel);
        // Ở đây bạn có thể gọi gameManager.loadLevel("level" + currentLevel + ".txt");
        //gameManager.loadLevel("level" + currentLevel + ".txt");
        Continue = false;
        gameManager.startGame();
        startGame();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
