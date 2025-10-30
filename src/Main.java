//package Arkanoid;
//
//import controller.GameManager;
//import controller.SaveLoadGame;
//import javafx.scene.image.Image;
//import view.*;
//import model.GameObject;
//import javafx.animation.AnimationTimer;
//import javafx.application.Application;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import java.util.*;
//
//public class Main extends Application {
//
//    public boolean Continue = false;
//    private SaveLoadGame saveLoadGame = new SaveLoadGame();
//    public static String ballImage;
//    public static String paddleImage;
//    private static final int WIDTH = 800;
//    private static final int HEIGHT = 600;
//
//    private Stage primaryStage;
//    private Scene menuScene, gameScene, endScene, pauseScene;
//
//    private Renderer renderer;
//    private GameManager gameManager;
//    private AnimationTimer gameLoop;
//    private Set<KeyCode> activeKeys = new HashSet<>();
//
//    private boolean isPaused = false;
//    public static int currentLevel = 1;
//
//    private StartScreen startScreen = new StartScreen();
//    private EndScreen endScreen = new EndScreen();
//    private PauseScreen pauseScreen = new PauseScreen();
//
//    @Override
//    public void start(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//        primaryStage.setTitle("Arkanoid Clone (JavaFX)");
//        primaryStage.setResizable(false);
//
//        showMenu();
//        primaryStage.show();
//    }
//
//    // ------------------ MENU ------------------
//    private void showMenu() {
//        Scene menuSceneFromStartScreen = startScreen.getScene(primaryStage, this);
//        //startScreen.getStartGameButton().setOnAction(e -> startGame());
//        //startScreen.getExitButton().setOnAction(e -> System.exit(0));
//        primaryStage.setScene(menuSceneFromStartScreen);
//    }
//
//
//    // ------------------ GAME ------------------
//    public void startGame() {
//        Canvas canvas = new Canvas(WIDTH, HEIGHT);
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        renderer = new Renderer(gc);
//        gameManager = new GameManager(renderer);
//        Pane root = new Pane(canvas);
//        gameScene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
//        gameManager.initializeGame();
//        primaryStage.setScene(gameScene);
//        if (Continue) {
//            saveLoadGame.loadGame(gameManager);
//            System.out.println(gameManager.getLives());
//        } else {
//            gameManager.loadLevel("level" + currentLevel + ".txt");
//            gameManager.getBall().setImage(ballImage);
//            gameManager.getPaddle().setImage(paddleImage);
//        }
//
//        // Xử lý phím
//        gameScene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ESCAPE) {
//                togglePause();
//                return;
//            }
//            activeKeys.add(event.getCode());
//            gameManager.handleInput(event.getCode().ordinal());
//        });
//        gameScene.setOnKeyReleased(event -> {
//            activeKeys.remove(event.getCode());
//            gameManager.handleKeyReleased(event.getCode().ordinal());
//            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT)
//                gameManager.getPaddle().stop();
//        });
//
//
//        gameManager.startGame();
//
//        // Vòng lặp game
//        gameLoop = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (isPaused) return; // Dừng cập nhật khi pause
//
//                gc.setFill(Color.BLACK);
//                gc.fillRect(0, 0, WIDTH, HEIGHT);
//
//                gameManager.updateGame();
//
//                // Thu thập và vẽ
//                List<GameObject> objects = new ArrayList<>();
//                objects.add(gameManager.getPaddle());
//                objects.add(gameManager.getBall());
//                objects.addAll(gameManager.getBricks());
//                objects.addAll(gameManager.getPowerUps());
//                renderer.draw(objects, gameManager.getScore(), gameManager.getLives());
//
//                // Kiểm tra kết thúc
//                if (gameManager.getGameState() == GameManager.GameState.GAME_OVER ||
//                        gameManager.getGameState() == GameManager.GameState.LEVEL_COMPLETE) {
//                    saveLoadGame.saveGame(gameManager);
//                    stop();
//                    boolean win = (gameManager.getGameState() == GameManager.GameState.LEVEL_COMPLETE);
//                    showEndScreen(gameManager.getScore(), win);
//                }
//            }
//        };
//
//        gameLoop.start();
//    }
//
//
//    // ------------------ PAUSE ------------------
//    private void togglePause() {
//        if (isPaused) {
//            isPaused = false;
//            primaryStage.setScene(gameScene); // Quay lại scene game
//            gameLoop.start(); // Khởi động lại game loop
//        } else {
//            isPaused = true;
//            gameLoop.stop(); // Dừng game loop khi pause
//            showPauseScreen();
//        }
//    }
//
//    private void showPauseScreen() {
//        Scene pauseScene = pauseScreen.getScene(primaryStage, WIDTH, HEIGHT);
//
//        saveLoadGame.saveGame(gameManager);
//        pauseScreen.getContinueButton().setOnAction(e -> togglePause());
//        pauseScreen.getExitToMenuButton().setOnAction(e -> {
//            gameLoop.stop();
//            isPaused = false; // Đảm bảo trạng thái không bị pause khi về menu
//            currentLevel = 1;
//            showMenu();
//        });
//
//        primaryStage.setScene(pauseScene);
//    }
//
//    // ------------------ END GAME ------------------
//    private void showEndScreen(int score, boolean win) {
//        endScreen.setMessage(win ? "🎉 LEVEL COMPLETE!" : "💀 GAME OVER!");
//        endScreen.setScore(score);
//
//        Scene endScene = endScreen.getScene(primaryStage, WIDTH, HEIGHT, win);
//
//        endScreen.getRestartButton().setOnAction(e -> {
//            currentLevel = 1;
//            showMenu();
//        });
//
//        endScreen.getExitToMenuButton().setOnAction(e -> {
//            currentLevel = 1;
//            showMenu();
//        });
//
//        // Chỉ gắn hành động cho Next Level khi có nút này
//        if (win) {
//            saveLoadGame.saveGame(gameManager);
//            endScreen.getNextLevelButton().setOnAction(e -> startNextLevel());
//        }
//
//        primaryStage.setScene(endScene);
//    }
//
//    // ------------------ NEXT LEVEL ------------------
//    private void startNextLevel() {
//        currentLevel++;
//        System.out.println("Starting level " + currentLevel);
//        // Ở đây bạn có thể gọi gameManager.loadLevel("level" + currentLevel + ".txt");
//        //gameManager.loadLevel("level" + currentLevel + ".txt");
//        Continue = false;
//        gameManager.startGame();
//        startGame();
//
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}



// Arkanoid/Main.java
package Arkanoid;

import controller.SoundManager;
import controller.GameManager;
import controller.SaveLoadGame;
import javafx.scene.image.Image;
import view.*; // Import tất cả các view
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    public boolean Continue = false;
    private SaveLoadGame saveLoadGame = new SaveLoadGame();
    public static String ballImage; // Sẽ được set bởi SelectBall
    public static String paddleImage; // Sẽ được set bởi SelectPaddle

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Stage primaryStage;
    private Scene gameScene; // Các scene khác do các lớp View quản lý

    // Đối tượng cho P1
    private Renderer rendererP1;
    private GameManager gameManagerP1;
    private GraphicsContext gcP1;

    // Đối tượng cho P2
    private Renderer rendererP2;
    private GameManager gameManagerP2;
    private GraphicsContext gcP2;

    private GameManager.GameMode currentGameMode; // Lưu chế độ đang chơi

    private AnimationTimer gameLoop;
    private Set<KeyCode> activeKeys = new HashSet<>();

    private boolean isPaused = false;
    public static int currentLevel = 1;

    // Khởi tạo các màn hình
    private StartScreen startScreen = new StartScreen();
    private EndScreen endScreen = new EndScreen();
    private PauseScreen pauseScreen = new PauseScreen();
    // (Menu, SelectBall, SelectPaddle sẽ được tạo khi cần)

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Arkanoid Clone (JavaFX)");
        primaryStage.setResizable(false);
        showMenu(); // Bắt đầu game bằng cách hiển thị StartScreen
        primaryStage.show();
    }

    // ------------------ QUẢN LÝ LUỒNG UI (MỚI) ------------------

    /**
     * 1. Hiển thị màn hình "Press Start"
     */
    public void showMenu() {
        Scene scene = startScreen.getScene(primaryStage, this);
        primaryStage.setScene(scene);
    }

    /**
     * 2. Hiển thị Menu chính (New Game, Continue, 2 Player)
     * (Hàm này được gọi từ StartScreen)
     */
    public void showMainMenu() {
        Menu menu = new Menu();
        Scene scene = menu.getScene(primaryStage, this);
        primaryStage.setScene(scene);
    }

    /**
     * 3. Hiển thị chọn bóng (truyền chế độ 1P/2P)
     * (Hàm này được gọi từ Menu)
     */
    public void showSelectBallScreen(GameManager.GameMode mode) {
        SelectBall selectBall = new SelectBall();
        Scene scene = selectBall.getScene(primaryStage, this,mode);
        primaryStage.setScene(scene);
    }

    /**
     * 4. Hiển thị chọn paddle (truyền chế độ 1P/2P)
     * (Hàm này được gọi từ SelectBall)
     */
    public void showSelectPaddleScreen(GameManager.GameMode mode) {
        SelectPaddle selectPaddle = new SelectPaddle();
        Scene scene = selectPaddle.getScene(primaryStage, this, mode);
        primaryStage.setScene(scene);
    }


    // ------------------ GAME ------------------
    /**
     * 5. Bắt đầu game (được gọi từ SelectPaddle hoặc Menu-Continue)
     */
    public void startGame(GameManager.GameMode mode) {
        SoundManager.playSound(SoundManager.SOUND_GAME_START);
        this.currentGameMode = mode;
        activeKeys.clear();
        isPaused = false;

        Pane root;
        Canvas canvasP1;
        Canvas canvasP2;

        if (mode == GameManager.GameMode.SINGLE_PLAYER) {
            // --- Chế độ 1 người ---
            canvasP1 = new Canvas(WIDTH, HEIGHT);
            gcP1 = canvasP1.getGraphicsContext2D();
            rendererP1 = new Renderer(gcP1);
            gameManagerP1 = new GameManager(rendererP1);

            root = new Pane(canvasP1);
            gameScene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

            gameManagerP1.initializeGame();
            if (Continue) {
                saveLoadGame.loadGame(gameManagerP1);
            } else {
                gameManagerP1.loadLevel("level" + currentLevel + ".txt");
                if (ballImage != null) gameManagerP1.getBall().setImage(ballImage);
                if (paddleImage != null) gameManagerP1.getPaddle().setImage(paddleImage);
            }

        } else {
            // --- Chế độ 2 người ---
            canvasP1 = new Canvas(WIDTH, HEIGHT);
            canvasP2 = new Canvas(WIDTH, HEIGHT);

            gcP1 = canvasP1.getGraphicsContext2D();
            rendererP1 = new Renderer(gcP1);
            gameManagerP1 = new GameManager(rendererP1);

            gcP2 = canvasP2.getGraphicsContext2D();
            rendererP2 = new Renderer(gcP2);
            gameManagerP2 = new GameManager(rendererP2);

            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().addAll(canvasP2,canvasP1);

            root = hbox;
            gameScene = new Scene(root, WIDTH * 2 + 10, HEIGHT, Color.BLACK);

            gameManagerP1.initializeGame();
            gameManagerP2.initializeGame();

            String mapFile = "level" + currentLevel + ".txt";
            gameManagerP1.loadLevel(mapFile);
            gameManagerP2.loadLevel(mapFile);

            // Set skin cho cả 2
            if (ballImage != null) {
                gameManagerP1.getBall().setImage(ballImage);
                gameManagerP2.getBall().setImage(ballImage);
            }
            if (paddleImage != null) {
                gameManagerP1.getPaddle().setImage(paddleImage);
                gameManagerP2.getPaddle().setImage(paddleImage);
            }
        }

        primaryStage.setScene(gameScene);

        // --- Xử lý phím (VIẾT LẠI) ---
        gameScene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());

            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
                return;
            }
            // P1 Launch Ball (Enter)
            if (event.getCode() == KeyCode.ENTER) {
                gameManagerP1.handleInput(KeyCode.ENTER.ordinal());
            }
            if (currentGameMode == GameManager.GameMode.TWO_PLAYER) {
                // P2 Launch Ball (W)
                if (event.getCode() == KeyCode.W) {
                    gameManagerP2.handleInput(KeyCode.ENTER.ordinal());
                }
            }
        });

        gameScene.setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode());
        });


        gameManagerP1.startGame();
        if (currentGameMode == GameManager.GameMode.TWO_PLAYER) {
            gameManagerP2.startGame();
        }

        // --- Vòng lặp game (VIẾT LẠI) ---
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) return;

                System.out.println(gameManagerP1.getBall().getSpeed());
                // --- 1. Xử lý Input (liên tục) ---
                // P1: Dùng phím Mũi tên
                if (activeKeys.contains(KeyCode.LEFT)) gameManagerP1.getPaddle().moveLeft();
                else if (activeKeys.contains(KeyCode.RIGHT)) gameManagerP1.getPaddle().moveRight();
                else gameManagerP1.getPaddle().stop();

                if (currentGameMode == GameManager.GameMode.TWO_PLAYER) {
                    // P2: Dùng phím A / D
                    if (activeKeys.contains(KeyCode.A)) gameManagerP2.getPaddle().moveLeft();
                    else if (activeKeys.contains(KeyCode.D)) gameManagerP2.getPaddle().moveRight();
                    else gameManagerP2.getPaddle().stop();
                }

                // --- 2. Cập nhật Logic Game ---
                gameManagerP1.updateGame();
                if (currentGameMode == GameManager.GameMode.TWO_PLAYER) {
                    gameManagerP2.updateGame();
                }

                // --- 3. Vẽ (Render) ---

                // Tối ưu hóa: Main sẽ xóa màn hình, Renderer chỉ vẽ.
                gcP1.setFill(Color.BLACK);
                gcP1.fillRect(0, 0, WIDTH, HEIGHT);
                rendererP1.draw(getAllObjects(gameManagerP1), gameManagerP1.getScore(), gameManagerP1.getLives());

                if (currentGameMode == GameManager.GameMode.TWO_PLAYER) {
                    gcP2.setFill(Color.BLACK);
                    gcP2.fillRect(0, 0, WIDTH, HEIGHT);
                    rendererP2.draw(getAllObjects(gameManagerP2), gameManagerP2.getScore(), gameManagerP2.getLives());
                }

                // --- 4. Kiểm tra Thắng/Thua ---
                if (currentGameMode == GameManager.GameMode.TWO_PLAYER) {
                    boolean p1Lost = gameManagerP1.getGameState() == GameManager.GameState.GAME_OVER;
                    boolean p1Won = gameManagerP1.getGameState() == GameManager.GameState.LEVEL_COMPLETE;
                    boolean p2Lost = gameManagerP2.getGameState() == GameManager.GameState.GAME_OVER;
                    boolean p2Won = gameManagerP2.getGameState() == GameManager.GameState.LEVEL_COMPLETE;

                    if (p1Won || p2Lost) {
                        stop();
                        showEndScreen(gameManagerP1.getScore(), false, "🏆 PLAYER 1 WINS! 🏆");
                    }
                    else if (p2Won || p1Lost) {
                        stop();
                        showEndScreen(gameManagerP2.getScore(), false, "🏆 PLAYER 2 WINS! 🏆");
                    }

                } else { // Chế độ 1 người
                    if (gameManagerP1.getGameState() == GameManager.GameState.GAME_OVER ||
                            gameManagerP1.getGameState() == GameManager.GameState.LEVEL_COMPLETE) {

                        if (Continue) saveLoadGame.saveGame(gameManagerP1);

                        stop();
                        boolean win = (gameManagerP1.getGameState() == GameManager.GameState.LEVEL_COMPLETE);
                        showEndScreen(gameManagerP1.getScore(), win, win ? "🎉 LEVEL COMPLETE!" : "💀 GAME OVER!");
                    }
                }
            }
        };

        gameLoop.start();
    }

    // --- HÀM HELPER: Gom đối tượng để vẽ ---
    private List<GameObject> getAllObjects(GameManager gm) {
        List<GameObject> objects = new ArrayList<>();
        objects.add(gm.getPaddle());
        objects.add(gm.getBall());
        objects.addAll(gm.getBricks());
        objects.addAll(gm.getPowerUps());
        objects.addAll(gm.getSteels());
        return objects;
    }


    // ------------------ PAUSE ------------------
    private void togglePause() {
        if (isPaused) {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            isPaused = false;
            primaryStage.setScene(gameScene);
            gameLoop.start();
        } else {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            isPaused = true;
            gameLoop.stop();
            showPauseScreen();
        }
    }

    private void showPauseScreen() {
        // PauseScreen chỉ có kích thước 800x600,
        // nhưng nó sẽ tạm che scene 1600x600, như vậy là ổn.
        Scene pauseScene = pauseScreen.getScene(primaryStage, WIDTH, HEIGHT);

        // Chỉ lưu game P1
        if (currentGameMode == GameManager.GameMode.SINGLE_PLAYER) {
            saveLoadGame.saveGame(gameManagerP1);
        }

        pauseScreen.getContinueButton().setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            togglePause();
        });
        pauseScreen.getExitToMenuButton().setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            gameLoop.stop();
            isPaused = false;
            currentLevel = 1;
            showMenu(); // Quay về StartScreen
        });

        primaryStage.setScene(pauseScene);
    }

    // ------------------ END GAME ------------------
    private void showEndScreen(int score, boolean win, String message) {
        endScreen.setMessage(message); // <-- Dùng message tùy chỉnh
        endScreen.setScore(score);

        // (Bạn cần có file EndScreen.java)
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

        // Chỉ cho phép Next Level ở chế độ 1 người
        if (win && currentGameMode == GameManager.GameMode.SINGLE_PLAYER) {
            SoundManager.playSound(SoundManager.SOUND_WIN_GAME);
            if (Continue) saveLoadGame.saveGame(gameManagerP1);

            if (endScreen.getNextLevelButton() != null) {
                endScreen.getNextLevelButton().setOnAction(e ->
                {
                    SoundManager.playSound(SoundManager.SOUND_CLICK);
                    startNextLevel();
                });
            }
        }

        primaryStage.setScene(endScene);
    }

    // ------------------ NEXT LEVEL ------------------
    private void startNextLevel() {
        SoundManager.playSound(SoundManager.SOUND_GAME_START);
        currentLevel++;
        System.out.println("Starting level " + currentLevel);
        Continue = false;
        // Next level sẽ đưa bạn về chọn skin
        showSelectBallScreen(GameManager.GameMode.SINGLE_PLAYER);
    }

    public static void main(String[] args) {
        launch(args);
    }
}