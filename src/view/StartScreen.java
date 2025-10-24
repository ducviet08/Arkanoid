package view;

import javafx.geometry.Insets; // Import lớp Insets
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartScreen {
    public Scene getScene(Stage stage) {
        Pane root = new Pane();
        Image bgImage = new Image("/images/background.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false
                )
        );
        // button press start
        Button PressStart =  new Button("Press Start");
        PressStart.setStyle("-fx-font-size: 30;" +
                " -fx-text-fill: black;" +
                " -fx-background-color: lightblue;" +
                "-fx-borfer-color : transparent;" +
                "-fx-focus-color: transparent;" +
                "-fx-background-radius:30;" +
                "-fx-border-radius:30;"
        );
        PressStart.setLayoutX(300);
        PressStart.setLayoutY(300);
        //action cho pressStart
        PressStart.setOnAction(e -> {
            Menu menu = new Menu();
            stage.setScene(menu.getScene(stage));
        });

        root.setBackground(new Background(backgroundImage));
        root.getChildren().addAll(PressStart);
        Scene scene = new Scene(root, 800, 600);

        // Load CSS đúng cách
        scene.getStylesheets().add(getClass().getResource("/screen/style.css").toExternalForm());

        return scene;
    }
}