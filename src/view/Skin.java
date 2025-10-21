package Arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Skin {
    public Scene getScene(Stage stage) {
        //select ball
        ChoiceBox<ImageView> selectBall = new ChoiceBox<>();
        ImageView ball1 = new ImageView(new Image(getClass().getResource("normal_ball.png").toExternalForm()));
        ImageView ball2 = new ImageView(new Image(getClass().getResource("fast_ball.png").toExternalForm()));
        ImageView ball3 = new ImageView(new Image(getClass().getResource("slow_ball.png").toExternalForm()));

        selectBall.getItems().addAll(ball1, ball2, ball3);
        //default skin
        selectBall.setValue(ball1);

        selectBall.setLayoutX(100);
        selectBall.setLayoutY(100);
        //button startGame
        Button button4 = new Button("SELECT");

        button4.setStyle("-fx-font-size: 30; -fx-text-fill: #1a5aec; -fx-background-color: transparent;");

        button4.setLayoutX(350);
        button4.setLayoutY(350);


        Pane root = new Pane();
        root.getStyleClass().add("bg-with-border");
        root.getChildren().addAll(selectBall);
        root.getChildren().addAll(button4);

        Scene scene = new Scene(root,800,600);
        scene.getStylesheets().add(getClass().getResource("/screen/style.css").toExternalForm());
        return scene;
    }
}

