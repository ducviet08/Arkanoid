package Arkanoid.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;

public class PauseScreen {
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
        Button remuse = new Button("Remuse");
        remuse.setStyle("-fx-font-size: 25; -fx-text-fill: gray; -fx-background-color: transparent;");
        remuse.setLayoutX(330);
        remuse.setLayoutY(350);

        remuse.setOnAction(e -> {
            Round round = new Round();
            stage.setScene(round.getScene(stage));
        });

        Button exit = new Button("Exit");
        exit.setStyle("-fx-font-size: 25; -fx-text-fill: gray; -fx-background-color: transparent;");
        exit.setLayoutX(350);
        exit.setLayoutY(450);

        exit.setOnAction(e -> {
            stage.close();
        });

        Pane root = new Pane();
        root.setBackground(new Background(bg));
        root.getChildren().addAll(remuse, exit);
        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                remuse.fire();
            }
        });
        return scene;
    }
}
