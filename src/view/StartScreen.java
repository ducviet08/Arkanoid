package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StartScreen {
    public Scene getScene(Stage stage) {

        // button press start
        Button PressStart =  new Button("... Press Start ...");
        PressStart.setStyle("-fx-font-size: 30; -fx-text-fill: #1a5aec; -fx-background-color: transparent;");
        PressStart.setLayoutX(280);
        PressStart.setLayoutY(300);
        //action cho pressStart
        PressStart.setOnAction(e -> {
            Menu menu = new Menu();
            stage.setScene(menu.getScene(stage));
        });
        Pane root = new Pane();
        root.getStyleClass().add("bg-with-border");
        root.getChildren().addAll(PressStart);
        Scene scene = new Scene(root, 800, 600);

        // Load CSS đúng cách
        scene.getStylesheets().add(getClass().getResource("/screen/style.css").toExternalForm());

        return scene;
    }
}