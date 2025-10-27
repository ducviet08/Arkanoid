package view;

import Arkanoid.Main;
import javafx.geometry.Insets; // Import lớp Insets
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartScreen {
    public Scene getScene(Stage stage, Main main) {
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

        // --- 1. CSS Mặc định ---
        final String defaultStyle = "-fx-font-size: 30;" +
                " -fx-text-fill: black;" +
                " -fx-background-color: lightblue;" +
                " -fx-border-color: transparent;" + // Đã sửa lỗi chính tả từ borfer_color
                " -fx-focus-color: transparent;" +
                " -fx-background-radius: 30;" +
                " -fx-border-radius: 30;";

        // --- 2. CSS Hover (Phát sáng) ---
        final String hoverStyle = " -fx-background-color: #A0FFFF;" + // Màu sáng hơn
                " -fx-scale-x: 1.05;" +             // Phóng to nhẹ
                " -fx-scale-y: 1.05;" +
                " -fx-effect: dropshadow(three-pass-box, yellow, 10, 0.5, 0, 0);"; // Hiệu ứng bóng sáng

        // button press start
        Button PressStart = new Button("Press Start");

        // Áp dụng style mặc định ban đầu
        PressStart.setStyle(defaultStyle);

        PressStart.setLayoutX(300);
        PressStart.setLayoutY(300);

        // --- 3. Xử lý sự kiện Hover ---
        PressStart.setOnMouseEntered(e -> {
            PressStart.setStyle(defaultStyle + hoverStyle);
        });

        PressStart.setOnMouseExited(e -> {
            PressStart.setStyle(defaultStyle);
        });

        // action cho pressStart
        PressStart.setOnAction(e -> {
            Menu menu = new Menu();
            stage.setScene(menu.getScene(stage, main));
        });

        root.setBackground(new Background(backgroundImage));
        root.getChildren().addAll(PressStart);
        Scene scene = new Scene(root, 800, 600);

        return scene;
    }
}