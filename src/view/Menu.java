package view;

import Arkanoid.Main;
import controller.SaveLoadGame;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class Menu {
    // tạo button
    private Button createButton(String a, double x, double y) {
        Button btn = new Button(a);
        btn.setLayoutX(x);
        btn.setLayoutY(y);

        // CSS mặc định
        String defaultStyle = "-fx-font-size: 30;" +
                " -fx-text-fill: black;" +
                " -fx-background-color: lightblue;" +
                " -fx-border-color: transparent;" +
                " -fx-focus-color: transparent;" +
                " -fx-background-radius: 30;" +
                " -fx-border-radius: 30;";

        // CSS cho trạng thái HOVER (Phát sáng)
        String hoverStyle = "-fx-background-color: #A0FFFF;" + // Màu sáng hơn
                " -fx-scale-x: 1.05;" +             // Phóng to nhẹ
                " -fx-scale-y: 1.05;" +
                " -fx-effect: dropshadow(three-pass-box, yellow, 10, 0.5, 0, 0);"; // Hiệu ứng bóng (phát sáng)

        // Áp dụng CSS mặc định
        btn.setStyle(defaultStyle);

        // Xử lý sự kiện di chuột vào (MOUSE_ENTERED)
        btn.setOnMouseEntered(e -> {
            btn.setStyle(defaultStyle + hoverStyle);
        });

        // Xử lý sự kiện di chuột ra (MOUSE_EXITED)
        btn.setOnMouseExited(e -> {
            btn.setStyle(defaultStyle); // Quay lại style mặc định
        });

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
        //NewGame
        Button button1 = createButton("NEW GAME", 305, 230);
        //Continue
        Button button2 = createButton("CONTINUE", 310, 350);
        //HighScore
        Button button3 = createButton("HIGH SCORES", 295, 470);


        //action pick skin
        button1.setOnAction(e -> {
            Select_ball selectBall = new Select_ball(); // tạo đối tượng mới
            stage.setScene(selectBall.getScene(stage, main)); // gọi phương thức thông qua đối tượng
        });

        button2.setOnAction(e -> {
            SaveLoadGame saveLoadGame = new SaveLoadGame();
            if (saveLoadGame.getLives() != 0) {
                main.Continue = true;
                main.startGame();
            }
        });
        button3.setOnAction(e -> {
            HighScore highScore = new HighScore();
            stage.setScene(highScore.getScene(stage,0));
        });
        root.getChildren().addAll(button1, button2, button3);
        root.setBackground(new Background(backgroundImage));

        //tạo scene 800x600
        Scene scene = new Scene(root, 800, 600);

        // Load CSS đúng cách

        return scene;
    }
}
