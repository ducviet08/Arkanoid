package view;

import Arkanoid.Main;
import controller.GameManager;
import controller.SaveLoadGame;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Menu {
    // tạo button
    private Button createButton(String path, double x, double y,int a,int b) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(a);
        view.setFitHeight(b);
        view.setPreserveRatio(true);

        Button btn = new Button();
        btn.setGraphic(view);
        btn.setStyle("-fx-font-size: 30;" +
                " -fx-text-fill: black;" +
                " -fx-background-color: transparent;" +
                "-fx-border-color : transparent;" +
                "-fx-focus-color: transparent;"
        );
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        return btn;
    }


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
        //NewGame
        Button button1 = createButton("/images/newgame.png", 60, 250,450,120);
        //Continue
        Button button2 = createButton("/images/continue.png", 460, 250,450,120);
        //HighScore
        Button button3 = createButton("/images/highscorebutton.png", 260, 350,450,117);


        //action pick skin
        button1.setOnAction(e -> {
            SelectBall selectBall = new SelectBall(); // tạo đối tượng mới
            stage.setScene(selectBall.getScene(stage)); // gọi phương thức thông qua đối tượng
        });
        button3.setOnAction(e -> {
            HighScore highScore = new HighScore();
            stage.setScene(highScore.getScene(stage));
        });
        button2.setOnAction(e -> {
            Round round = new Round();
            stage.setScene(round.getScene(stage));
        });
        root.getChildren().addAll(button1, button2, button3);
        root.setBackground(new Background(backgroundImage));

        //tạo scene 800x600
        Scene scene = new Scene(root, 800, 600);

        // Load CSS đúng cách

        return scene;
    }
}
