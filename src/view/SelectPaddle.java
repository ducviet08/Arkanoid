// Arkanoid/view/SelectPaddle.java
package view;

import controller.SoundManager;
import Arkanoid.Main;
import controller.GameManager; // <-- IMPORT QUAN TRỌNG
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static Arkanoid.Main.paddleImage;

public class SelectPaddle {
    private String pathPaddle;

    private Button createpaddleButton(String path, double x, double y) {
        // ... (Hàm này giữ nguyên) ...
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(100);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        Button btn = new Button();
        btn.setGraphic(view);
        final String defaultStyle = "-fx-background-color: transparent;";
        final String hoverStyle = "-fx-scale-x: 1.2;" +
                " -fx-scale-y: 1.2;" +
                " -fx-effect: dropshadow(three-pass-box, white, 15, 0.8, 0, 0);";
        btn.setStyle(defaultStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(defaultStyle + hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        return btn;
    }

    // --- CẬP NHẬT: Thêm 'GameManager.GameMode mode' ---
    public Scene getScene(Stage stage, Main main, GameManager.GameMode mode) {
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

        // Thêm 2 paddle
        Button paddle1 = createpaddleButton("/images/paddle.png", 350, 350);
        Button paddle2 = createpaddleButton("/images/paddle2.png", 350, 450); // Nút thứ 2

        // --- CẬP NHẬT: Handler ---
        EventHandler<ActionEvent> handler = event -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            Button clicked = (Button) event.getSource();

            if (clicked == paddle1) {
                pathPaddle = "/images/paddle.png";
            } else if (clicked == paddle2) { // Xử lý nút thứ 2
                pathPaddle = "/images/paddle2.png";
            }

            paddleImage = pathPaddle; // Set biến static
            main.Continue = false;

            // BẮT ĐẦU GAME VỚI CHẾ ĐỘ ĐÃ CHỌN
            main.startGame(mode);
        };
        // --- HẾT CẬP NHẬT HANDLER ---

        paddle1.setOnAction(handler);
        paddle2.setOnAction(handler); // Gán handler cho nút 2

        Scene scene = new Scene(root, 800, 600);
        root.setBackground(new Background(backgroundImage));
        root.getChildren().addAll(paddle1, paddle2); // Thêm nút 2 vào scene
        return scene;
    }
}