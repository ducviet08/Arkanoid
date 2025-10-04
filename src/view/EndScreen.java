package Arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class EndScreen {
    public Scene getScene(Stage stage) {
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
        Button MainMenu = new Button("Main Menu");
        Button exit = new Button("Exit");

        MainMenu.setOnAction(e -> {
            MenuScreen menuScreen = new MenuScreen();
            stage.setScene(menuScreen.getScene(stage));
        });

        exit.setOnAction(e -> {
            stage.close();
        });

        VBox root = new VBox(20, MainMenu, exit);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(bg));


        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        return scene;
    }
}
