// view/EndGameScreen.java
package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image; // Thêm import này
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class EndScreen {

    private Label messageLabel;
    private Label scoreLabel;
    private Button restartButton;
    private Button nextLevelButton;
    private Button exitToMenuButton;

    public EndScreen() {
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        scoreLabel = new Label();
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        restartButton = new Button("Restart");
        restartButton.setPrefSize(200, 60);
        restartButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;");

        nextLevelButton = new Button("Next Level");
        nextLevelButton.setPrefSize(200, 60);
        nextLevelButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;");

        exitToMenuButton = new Button("Exit to Menu");
        exitToMenuButton.setPrefSize(200, 60);
        exitToMenuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;");
    }

    // Setters để cập nhật nội dung
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setScore(int score) {
        scoreLabel.setText("Your Score: " + score);
    }

    // Getters cho các nút
    public Button getRestartButton() {
        return restartButton;
    }

    public Button getNextLevelButton() {
        return nextLevelButton;
    }

    public Button getExitToMenuButton() {
        return exitToMenuButton;
    }

    public Scene getScene(Stage stage, int width, int height, boolean win) {
        // --- Thêm phần code tải và thiết lập BackgroundImage ---
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
        // --------------------------------------------------------

        VBox buttonLayout = new VBox(20);
        buttonLayout.getChildren().addAll(messageLabel, scoreLabel);

        if (win) {
            buttonLayout.getChildren().add(nextLevelButton);
        }
        buttonLayout.getChildren().addAll(restartButton, exitToMenuButton);

        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setMaxWidth(Region.USE_PREF_SIZE);
        buttonLayout.setMaxHeight(Region.USE_PREF_SIZE);

        TextField inputBox = new TextField();
        inputBox.setLayoutX(100); // vị trí X
        inputBox.setLayoutY(150); // vị trí Y
        inputBox.setPrefWidth(200); // chiều rộng

        // nút lấy dữ liệu
        inputBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String text = inputBox.getText();
                System.out.println("Người dùng nhập: " + text);
                inputBox.setVisible(false);

            }
        });

        StackPane root = new StackPane(buttonLayout);
        root.setBackground(new Background(backgroundImage)); // Gắn background vào root StackPane
        // root.setStyle("-fx-background-color: black;"); // Bỏ dòng này để ảnh nền hiển thị

        Scene scene = new Scene(root, width, height);
        return scene;
    }
}