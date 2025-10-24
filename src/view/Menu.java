package view;

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
        private Button createButton(String a, double x, double y) {
            Button btn = new Button(a);
            btn.setLayoutX(x);
            btn.setLayoutY(y);
            btn.setStyle("-fx-font-size: 30;" +
                    " -fx-text-fill: black;" +
                    " -fx-background-color: lightblue;" +
                    "-fx-borfer-color : transparent;" +
                    "-fx-focus-color: transparent;" +
                    "-fx-background-radius:30;" +
                    "-fx-border-radius:30;"
            );
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
            Button button1 = createButton("NEW GAME", 305, 230);
            //Continue
            Button button2 = createButton("CONTINUE", 310, 350);
            //HighScore
            Button button3 = createButton("HIGH SCORES", 295, 470);


            //action pick skin
            button1.setOnAction(e -> {
                SelectBall selectBall = new SelectBall(); // tạo đối tượng mới
                stage.setScene(selectBall.getScene(stage)); // gọi phương thức thông qua đối tượng
            });

            root.getChildren().addAll(button1, button2, button3);
            root.setBackground(new Background(backgroundImage));

            //tạo scene 800x600
            Scene scene = new Scene(root, 800, 600);

            // Load CSS đúng cách
            scene.getStylesheets().add(getClass().getResource("/screen/style.css").toExternalForm());

            return scene;
        }
    }
