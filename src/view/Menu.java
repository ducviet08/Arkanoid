package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Menu {
    public Scene getScene(Stage stage) {

        Pane root = new Pane();
        //NewGame
        Button button1 = new Button("NEW GAME");
        button1.setStyle("-fx-font-size: 30; -fx-text-fill: #1a5aec; -fx-background-color: transparent;");
        button1.setLayoutX(100);
        button1.setLayoutY(350);
        //Continue
        Button button2 = new Button("CONTINUE");
        button2.setStyle("-fx-font-size: 30; -fx-text-fill: #1a5aec; -fx-background-color: transparent;");
        button2.setLayoutX(300);
        button2.setLayoutY(400);
        //HighScore
        Button button3 = new Button("HIGH SCORES");
        button3.setStyle("-fx-font-size: 30; -fx-text-fill: #1a5aec; -fx-background-color: transparent;");
        button3.setLayoutX(500);
        button3.setLayoutY(450);
        //action pick skin
//        button1.setOnAction(e -> {
//            Skin skin = new Skin();
//            stage.setScene(skin.getScene(stage));
//        });

        root.getStyleClass().add("bg-with-border");
        root.getChildren().add(button1);
        root.getChildren().add(button2);
        root.getChildren().add(button3);

        //tạo scene 800x600
        Scene scene = new Scene(root, 800, 600);

        // Load CSS đúng cách
        scene.getStylesheets().add(getClass().getResource("/screen/style.css").toExternalForm());

        return scene;
    }
}

