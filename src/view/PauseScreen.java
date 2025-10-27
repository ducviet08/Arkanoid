// view/PauseScreen.java
package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image; // Thêm import này
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class PauseScreen {

    private Button createButton(String path, double x, double y,int a,int b) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(a);
        view.setFitHeight(b);
        view.setPreserveRatio(true);

        Button btn = new Button();
        btn.setGraphic(view);
        btn.setStyle("-fx-background-color: transparent;");
        btn.setLayoutX(x);
        btn.setLayoutY(y);

        return btn;
    }
    public Scene getScene(Stage stage) {
        Pane  root = new Pane();
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
        Button play = createButton("/images/play.png", 250, 260,300,300);

        play.setOnAction(e -> {
            Round round = new Round();
            stage.setScene(round.getScene(stage));
        });

        Button exit = createButton("/images/exit.png", 70, 260,300,300);

        exit.setOnAction(e -> {
            Platform.exit();
        });

        Button highscore = createButton("/images/highscore.png", 290, 300,50,50);

        highscore.setOnAction(e -> {
            HighScore highScore = new HighScore();
            stage.setScene(highScore.getScene(stage));
        });

        root.setBackground(new Background(backgroundImage));
        root.getChildren().addAll(play, exit, highscore);

        Scene scene = new Scene(root, 800, 600);
        return  scene;
    }
}