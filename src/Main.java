// Arkanoid/Main.java
package Arkanoid;

import controller.GameManager;
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
    private AnimationTimer gameloopTimer;
    private Set<KeyCode>activeKeys =new HashSet<>();

    public void start(Stage primaryStage){
        primaryStage.setTitle("Arkanoid");
        primaryStage.setResizable(false);
        Canvas gameCanvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        renderer = new Renderer(gc);
        gameManager = new GameManager(renderer);
        Pane root = new Pane(gameCanvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());
            gameManager.handleInput(event.getCode().ordinal());
        });
        scene.setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode());
            gameManager.handleKeyReleased(event.getCode().ordinal());
            if(event.getCode() == KeyCode.LEFT||event.getCode() == KeyCode.RIGHT){
                gameManager.getPaddle().stop();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        gameManager.startGame();
        gameloopTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameManager.updateGame();
                List<GameObject> objectsToRender=new ArrayList<>();
                objectsToRender.add(gameManager.getPaddle());
                objectsToRender.add(gameManager.getBall());
                objectsToRender.addAll(gameManager.getBricks());
                objectsToRender.addAll(gameManager.getPowerUps());
                renderer.draw(objectsToRender,gameManager.getScore(),gameManager.getLives());
                if(gameManager.getGameState() == GameManager.GameState.GAME_OVER||
                gameManager.getGameState() == GameManager.GameState.LEVEL_COMPLETE){
                    this.stop();
                }


            }
        };
        gameloopTimer.start();
    }

    public static void main(String[] args) {
        //gọi javaFX để runtime
        launch(args);
    }
}