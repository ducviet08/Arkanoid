// Arkanoid//Menu.java
package Arkanoid.view;

import Arkanoid.controller.SoundManager;
import Arkanoid.Main;
import Arkanoid.controller.GameManager; // <-- IMPORT QUAN TRỌNG
import Arkanoid.controller.SaveLoadGame;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

        // CSS cho trạng thái HOVER
        String hoverStyle = "-fx-background-color: #A0FFFF;" +
                " -fx-scale-x: 1.05;" +
                " -fx-scale-y: 1.05;" +
                " -fx-effect: dropshadow(three-pass-box, yellow, 10, 0.5, 0, 0);";

        btn.setStyle(defaultStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(defaultStyle + hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));
        return btn;
    }

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
        //NewGame 1P
        Button button1 = createButton("1 PLAYER", 315, 200); // Đổi tên và vị trí
        //NewGame 2P (MỚI)
        Button button2P = createButton("2 PLAYERS", 305, 300); // Nút mới
        //Continue
        Button button2 = createButton("CONTINUE", 310, 400); // Đổi vị trí
        //HighScore
        Button button3 = createButton("HIGH SCORES", 295, 500); // Đổi vị trí


        // action 1 PLAYER (CẬP NHẬT)
        button1.setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            // Chuyển sang màn hình chọn bóng với chế độ 1P
            main.showSelectBallScreen(GameManager.GameMode.SINGLE_PLAYER);
        });

        // action 2 PLAYERS (MỚI)
        button2P.setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            // Chuyển sang màn hình chọn bóng với chế độ 2P
            main.showSelectBallScreen(GameManager.GameMode.TWO_PLAYER);
        });

        // action CONTINUE (Giữ nguyên)
        button2.setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            SaveLoadGame saveLoadGame = new SaveLoadGame();
            if (saveLoadGame.getLives() != 0) {
                main.Continue = true;
                // Continue luôn là 1P
                main.startGame(GameManager.GameMode.SINGLE_PLAYER);
            }
        });

        button3.setOnAction(e -> {
            SoundManager.playSound(SoundManager.SOUND_CLICK);
            HighScore highScore = new HighScore();
            stage.setScene(highScore.getScene(stage,0,main));
        });
        // action HighScore (Chưa làm gì)
        // button3.setOnAction(...);

        root.getChildren().addAll(button1, button2P, button2, button3); // Thêm button2P
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root, 800, 600);
        return scene;
    }
}