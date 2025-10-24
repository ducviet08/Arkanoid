package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SelectBall {
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
    public Scene getScene(Stage stage) {

        //button ball
        Button ball1 = createBallButton("/images/ball1.png", 200, 50);
        Button ball2 = createBallButton("/images/ball2.png", 300, 50);
        Button ball3 = createBallButton("/images/ball3.png", 400, 50);

        EventHandler<ActionEvent> handler = event ->{
            Select_paddle select_paddle = new Select_paddle();
            stage.setScene(select_paddle.getScene(stage));
        };


        //root
        Pane root = new Pane();
        root.getStyleClass().add("bg-with-border");
        root.getChildren().addAll(ball1, ball2, ball3);

        Scene scene = new Scene(root,800,600);
        scene.getStylesheets().add(getClass().getResource("/screen/style.css").toExternalForm());
        return scene;
    }
}
