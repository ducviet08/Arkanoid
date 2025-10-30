package view;

import Arkanoid.Main;
import controller.GameManager;
import controller.SoundManager;
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
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(100);
        view.setFitHeight(30);
        view.setPreserveRatio(true);

        Button btn = new Button();
        btn.setGraphic(view);

        // --- 1. Style Mặc định và Hover ---
        // Style mặc định: Nền trong suốt, không có hiệu ứng.
        final String defaultStyle = "-fx-background-color: transparent;";

        // Style Hover: Phóng to (scale) và thêm bóng đổ màu trắng (phát sáng).
        final String hoverStyle = "-fx-scale-x: 1.2;" + // Phóng to 20% theo chiều X
                " -fx-scale-y: 1.2;" + // Phóng to 20% theo chiều Y
                " -fx-effect: dropshadow(three-pass-box, white, 15, 0.8, 0, 0);"; // Bóng sáng trắng

        // Áp dụng style mặc định
        btn.setStyle(defaultStyle);

        // Xử lý sự kiện di chuột vào (MOUSE_ENTERED)
        btn.setOnMouseEntered(e -> {
            btn.setStyle(defaultStyle + hoverStyle);
        });

        // Xử lý sự kiện di chuột ra (MOUSE_EXITED)
        btn.setOnMouseExited(e -> {
            btn.setStyle(defaultStyle); // Quay lại style mặc định
        });
        // --- End Hover Logic ---

        btn.setLayoutX(x);
        btn.setLayoutY(y);
        return btn;
    }

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
        // round là cái bàm hình gameplay nhá

        Button paddle1 = createpaddleButton("/images/paddle.png", 350, 350);
        EventHandler<ActionEvent> handler = event -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            Button clicked = (Button) event.getSource(); // nút nào được nhấn
            ImageView view = (ImageView) clicked.getGraphic();
            Image img = view.getImage();
            if (clicked == paddle1) {
                pathPaddle = "/images/paddle.png";
            }
            paddleImage = pathPaddle;
            main.Continue = false;
            main.startGame();
        };

        paddle1.setOnAction(handler);
        Scene scene = new Scene(root, 800, 600);
        root.setBackground(new Background(backgroundImage));
        root.getChildren().addAll(paddle1);
        return scene;

    }
}