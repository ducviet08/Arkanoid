// view/EndGameScreen.java
package Arkanoid.view;

import Arkanoid.controller.GameManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.FileWriter;
import java.io.IOException;

public class EndScreen {

    private ImageView messageImageView;
    private Label scoreLabel;
    private Button restartButton;
    private Button nextLevelButton;
    private Button exitToMenuButton;
    private Button exit;
    private TextField nameField;  // ô nhập tên

    private int currentScore;

    // 🔹 Tạo nút có hình, vị trí (x, y), kích thước (a, b)
    private Button createbutton(String path, int x, int y, int a, int b) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(a);
        view.setFitHeight(b);
        view.setPreserveRatio(true);

        Button btn = new Button();
        btn.setGraphic(view);
        btn.setStyle("-fx-background-color: transparent;");
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        return btn;
    }

    public EndScreen() {
        messageImageView = new ImageView();
        messageImageView.setFitWidth(350);
        messageImageView.setFitHeight(150);
        messageImageView.setPreserveRatio(true);
        messageImageView.setLayoutX(225);
        messageImageView.setLayoutY(80);

        scoreLabel = new Label();
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        scoreLabel.setLayoutX(330);
        scoreLabel.setLayoutY(250);

        restartButton = createbutton("/images/restart.png", 360, 400, 80, 80);
        nextLevelButton = createbutton("/images/NextLevel.png", 330, 400, 120, 120);
        exitToMenuButton = createbutton("/images/Home.png", 240, 400, 80, 80);
        exit = createbutton("/images/exit.png", 470, 405, 80, 80);

        // 🧩 Tạo ô nhập tên
        nameField = new TextField();
        nameField.setPromptText("Enter your name...");
        nameField.setLayoutX(280);
        nameField.setLayoutY(330);
        nameField.setPrefWidth(240);
        nameField.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-font-size: 16px;");
    }

    // 🔹 Gán hình ảnh thắng hoặc thua
    public void setMessage(String message) {
        Image image = new Image(getClass().getResourceAsStream(message));
        messageImageView.setImage(image);
    }

    // 🔹 Hiển thị điểm và hiện ô nhập tên (chỉ Enter 1 lần)
    public void setScore(int score, Pane layout) {
        this.currentScore = score;
        scoreLabel.setText("Your Score: " + score);

        if (score >= 0) {
            layout.getChildren().add(nameField);

            // Khi nhấn Enter -> lưu 1 lần duy nhất
            nameField.setOnAction(e -> {
                String playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) {
                    saveHighScore(playerName, currentScore);
                    nameField.setEditable(false); // khóa ô nhập
                    nameField.setPromptText("Saved!");
                    nameField.setStyle("-fx-background-color: lightgray; -fx-font-size: 16px;");
                }
            });
        }
    }

    // 🔹 Lưu tên + điểm vào file
    private void saveHighScore(String name, int score) {
        try {
            // Lấy đường dẫn tuyệt đối tới thư mục project
            String projectPath = System.getProperty("user.dir");

            // Ghi vào file trong score.txt
            String filePath = projectPath + "/data/score.txt";

            FileWriter fw = new FileWriter(filePath, true);
            fw.write(name + "," + score + "\n");
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Button getRestartButton() {
        return restartButton;
    }

    public Button getNextLevelButton() {
        return nextLevelButton;
    }

    public Button getExitToMenuButton() {
        return exitToMenuButton;
    }

    // 🔹 Tạo Scene
    public Scene getScene(Stage stage, int width, int height, boolean win, int score, GameManager.GameMode mode) {
        Image bgImage = new Image(getClass().getResourceAsStream("images/background.png"));
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false
                )
        );

        Pane layout = new Pane();
        layout.setBackground(new Background(backgroundImage));

        layout.getChildren().addAll(
                messageImageView,
                scoreLabel,
                exitToMenuButton,
                exit
        );

        if (win) {
            layout.getChildren().add(nextLevelButton);
            nameField.setVisible(false);
        }
        if(!win){
            layout.getChildren().add(restartButton);
            nameField.setVisible(true);
        }

        // 🧩 Gán điểm & thêm TextField nhập tên
        if(mode == GameManager.GameMode.SINGLE_PLAYER){
            setScore(score, layout);
        }
        exit.setOnAction(e -> {
            Platform.exit();
        });

        return new Scene(layout, width, height);
    }
}
