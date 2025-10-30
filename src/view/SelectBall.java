package view;

import Arkanoid.Main;
import controller.SoundManager;
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

    private Button createBallButton(String path, double x, double y) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(50);
        view.setFitHeight(50);
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

        //button ball
        Button ball1 = createBallButton("/images/ball1.png", 200, 300);
        Button ball2 = createBallButton("/images/ball2.png", 350, 300);
        Button ball3 = createBallButton("/images/ball3.png", 500, 300);

        EventHandler<ActionEvent> handler = event -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            Button clicked = (Button) event.getSource(); // nút nào được nhấn
            ImageView view = (ImageView) clicked.getGraphic();
            Image img = view.getImage();

            // Lấy path tương ứng của nút đó
            if (clicked == ball1) pathBall = "/images/ball1.png";
            else if (clicked == ball2) pathBall = "/images/ball2.png";
            else if (clicked == ball3) pathBall = "/images/ball3.png";
            SelectPaddle select_paddle = new SelectPaddle();
            ballImage = pathBall;
            stage.setScene(select_paddle.getScene(stage, main));
        };
        ball1.setOnAction(handler);
        ball2.setOnAction(handler);
        ball3.setOnAction(handler);

        //root
        root.getChildren().addAll(ball1, ball2, ball3);
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root, 800, 600);
        return scene;
    }
}
