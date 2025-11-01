// Arkanoid//StartScreen.java
package Arkanoid.view;

import Arkanoid.controller.SoundManager;
import Arkanoid.Main; // Giữ import này
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
// (Các import khác giữ nguyên)

public class StartScreen {
    public Scene getScene(Stage stage, Main main) {
        Pane root = new Pane();
        Image bgImage = new Image(getClass().getResourceAsStream("/images/background.png"));
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false
                )
        );

        final String defaultStyle = "-fx-font-size: 30;" +
                " -fx-text-fill: black;" +
                " -fx-background-color: lightblue;" +
                " -fx-border-color: transparent;" +
                " -fx-focus-color: transparent;" +
                " -fx-background-radius: 30;" +
                " -fx-border-radius: 30;";
        final String hoverStyle = " -fx-background-color: #A0FFFF;" +
                " -fx-scale-x: 1.05;" +
                " -fx-scale-y: 1.05;" +
                " -fx-effect: dropshadow(three-pass-box, yellow, 10, 0.5, 0, 0);";

        Button PressStart = new Button("Press Start");
        PressStart.setStyle(defaultStyle);
        PressStart.setLayoutX(300);
        PressStart.setLayoutY(300);
        PressStart.setOnMouseEntered(e -> PressStart.setStyle(defaultStyle + hoverStyle));
        PressStart.setOnMouseExited(e -> PressStart.setStyle(defaultStyle));

        // --- CẬP NHẬT ---
        // Gọi hàm showMainMenu() MỚI của
        PressStart.setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            main.showMainMenu();
        });
        // --- HẾT CẬP NHẬT ---

        root.setBackground(new Background(backgroundImage));
        root.getChildren().addAll(PressStart);
        Scene scene = new Scene(root, 800, 600);

        return scene;
    }
}