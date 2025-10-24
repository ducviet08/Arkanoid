package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SelectPaddle {
    private Button createpaddleButton(String path, double x, double y) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(100);
        view.setFitHeight(30);
        view.setPreserveRatio(true);

        Button btn = new Button();
        btn.setGraphic(view);
        btn.setStyle("-fx-background-color: transparent;");
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
        // round là cái bàm hình gameplay nhá
        EventHandler<ActionEvent> handler = event -> {
            Round round = new Round();
            stage.setScene(round.getScene(stage));
        };
        Button paddle1 = createpaddleButton("/images/paddle1.png", 350, 350);

        paddle1.setOnAction(handler);

        Scene scene = new Scene(root, 800, 600);
        root.setBackground(new Background(backgroundImage));
        root.getChildren().addAll(paddle1);
        return scene;

    }
}