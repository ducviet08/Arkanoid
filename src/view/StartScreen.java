package Arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StartScreen {

    public Scene getScene(Stage stage) {
        // Tạo Image từ file ảnh
        Image img = new Image(getClass().getResource("BG.jpg").toExternalForm());
        // nhớ đúng đường dẫn file ảnh nhé
        // Tạo BackgroundImage từ img
        BackgroundImage bg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        Button button = new Button("Start Game");
        button.setStyle("-fx-font-size: 30px;");
        button.setPrefSize(200, 60);
        button.setLayoutX(300);
        button.setLayoutY(50);

        // Gắn background vào Pane
        Pane root = new Pane();
        root.setBackground(new Background(bg));
        root.getChildren().add(button);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
        return scene;
    }
}
