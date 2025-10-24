package view;

import Arkanoid.Main;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SelectBall {

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

    // tạo button
    private Button createBallButton(String path, double x, double y) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(50);
        view.setFitHeight(50);
        view.setPreserveRatio(true);

        Button btn = new Button();
        btn.setGraphic(view);
        btn.setStyle("-fx-background-color: transparent;");
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        return btn;
    }

    public Scene getScene(Stage stage,Main main) {

        //button ball
        Button ball1 = createBallButton("/images/ball1.png", 200, 300);
        Button ball2 = createBallButton("/images/ball2.png", 350, 300);
        Button ball3 = createBallButton("/images/ball3.png", 500, 300);

        EventHandler<ActionEvent> handler = event -> {
            SelectPaddle select_paddle = new SelectPaddle();
            stage.setScene(select_paddle.getScene(stage,main));
        };
        ball1.setOnAction(handler);
        ball2.setOnAction(handler);
        ball3.setOnAction(handler);

        //root
        root.getChildren().addAll(ball1, ball2, ball3);
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root, 800, 600);
        return scene;
    }
}
