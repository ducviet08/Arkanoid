package Arkanoid.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StartScreen {

    public Scene getScene(Stage stage) {
        // Tạo Image từ file ảnh
        Image img = new Image(getClass().getResource("BG.jpg").toExternalForm());
        BackgroundImage bg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        // Nút Start
        Button StartGame = new Button("Tap SPACE to start the game...");
        StartGame.setStyle("-fx-font-size: 25; -fx-text-fill: gray; -fx-background-color: transparent;");
        StartGame.setLayoutX(200);
        StartGame.setLayoutY(450);

        // Nút Exit
        Button exit = new Button("Exit");
        exit.setStyle("-fx-background-color: transparent;");


        // Pane gốc
        Pane root = new Pane();
        root.setBackground(new Background(bg));
        root.getChildren().addAll(StartGame, exit);

        // Action cho nút Start
        StartGame.setOnAction(e -> {
            Round round = new Round();
            stage.setScene(round.getScene(stage));
        });

        // Action cho nút Exit
        exit.setOnAction(e -> {
            Platform.exit(); // thoát hẳn JavaFX
        });

        Scene scene = new Scene(root, 800, 600);

        // Nhấn ESC = Exit
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                exit.fire();
            }
        });

        stage.setScene(scene);
        stage.show();
        return scene;
    }
}
