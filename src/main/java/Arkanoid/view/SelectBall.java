package Arkanoid.view;

import Arkanoid.controller.SoundManager;
import Arkanoid.Main;
import Arkanoid.controller.GameManager; // <-- IMPORT QUAN TRỌNG
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import static Arkanoid.Main.ballImage;

public class SelectBall {

    private String pathBall;
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

    private Button createBallButton(String path, double x, double y) {
        // ... (Hàm này giữ nguyên) ...
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(50);
        view.setFitHeight(50);
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

        Button ball1 = createBallButton("/images/ball1.png", 200, 300);
        Button ball2 = createBallButton("/images/ball2.png", 350, 300);
        Button ball3 = createBallButton("/images/ball3.png", 500, 300);

        // --- CẬP NHẬT: Handler ---
        EventHandler<ActionEvent> handler = event -> {
            Button clicked = (Button) event.getSource();
            ImageView view = (ImageView) clicked.getGraphic();
            Image img = view.getImage();

            SoundManager.playSound(SoundManager.SOUND_CLICK);
            if (clicked == ball1) pathBall = "/images/ball1.png";
            else if (clicked == ball2) pathBall = "/images/ball2.png";
            else if (clicked == ball3) pathBall = "/images/ball3.png";

            ballImage = pathBall; // Set biến static

            // Chuyển sang màn hình chọn Paddle,
            // VÀ TRUYỀN CHẾ ĐỘ (mode) ĐI TIẾP
            main.showSelectPaddleScreen(mode);
        };
        // --- HẾT CẬP NHẬT HANDLER ---

        ball1.setOnAction(handler);
        ball2.setOnAction(handler);
        ball3.setOnAction(handler);

        root.getChildren().addAll(ball1, ball2, ball3);
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root, 800, 600);
        return scene;
    }
}