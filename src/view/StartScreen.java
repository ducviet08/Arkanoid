package Arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StartScreen {

    public Scene getScene(Stage stage) {
        // Tạo Image từ file ảnh
        Image img = new Image(getClass().getResource("BG.jpg").toExternalForm());
        // nhớ đúng đường dẫn file ảnh nhé
        // Tạo BackgroundImage từ img
        BackgroundImage bg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        Button StartGame = new Button("Start Game");
        StartGame.setPrefSize(200, 60);
        StartGame.setLayoutX(300);
        StartGame.setLayoutY(50);

        Button exit = new Button("exit");
        exit.setLayoutX(300);
        exit.setLayoutY(300);

        // Gắn background vào Pane
        Pane root = new Pane();
        root.setBackground(new Background(bg));
        root.getChildren().addAll(StartGame,exit);

        StartGame.setOnAction(e -> {
            MenuScreen menuScreen = new MenuScreen();
            stage.setScene(menuScreen.getScene(stage));
        });
        exit.setOnAction(e -> {
            stage.close();
        });

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        return scene;
    }
}
