package view;

import Arkanoid.Main;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class StartScreen {
    public Scene getScene(Stage stage,Main main) {
        Pane root = new Pane();

        // Nền chính của game
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
        root.setBackground(new Background(backgroundImage));

        // Ảnh "Press Start" có nền trong suốt
        Image image = new Image(getClass().getResourceAsStream("/images/pressStart.png"));
        ImageView view = new ImageView(image);
        view.setFitWidth(250);   // vừa khung
        view.setPreserveRatio(true);

        Button pressStart = new Button();
        pressStart.setGraphic(view);
        pressStart.setStyle("-fx-background-color: transparent;");
        pressStart.setLayoutX(265);
        pressStart.setLayoutY(300);

        pressStart.setOnAction(e -> {
            Menu menu = new Menu();
            stage.setScene(menu.getScene(stage));
        });

        root.getChildren().add(pressStart);
        return new Scene(root, 800, 600);
    }
}