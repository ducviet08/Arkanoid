package Arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.effect.GaussianBlur;

public class Round {
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

        // Nút Pause
        Button pause = new Button("Pause");
        pause.setStyle("-fx-font-size: 25; -fx-text-fill: gray; -fx-background-color: transparent;");
        pause.setLayoutX(50);
        pause.setLayoutY(50);

        // Nút Play
        Button play = new Button("Play");
        play.setStyle("-fx-font-size: 25; -fx-text-fill: gray; -fx-background-color: transparent;");
        play.setLayoutX(350);
        play.setLayoutY(350);

        Pane root = new Pane();
        root.setBackground(new Background(bg));
        root.getChildren().addAll(pause, play);

        Scene scene = new Scene(root, 800, 600);

        // Bắt phím ESC và SPACE
        scene.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                pause.fire();    // ESC = Pause
            } else if (keyEvent.getCode() == KeyCode.SPACE) {
                play.fire();     // SPACE = EndScreen
            }
        });
        // Action Pause
        pause.setOnAction(e -> {
            PauseScreen pauseScreen = new PauseScreen();
            stage.setScene(pauseScreen.getScene(stage));
        });

        // Action Play (sang EndScreen)
        play.setOnAction(e -> {
            EndScreen endScreen = new EndScreen();
            stage.setScene(endScreen.getScene(stage));
        });

        return scene;
    }
}

