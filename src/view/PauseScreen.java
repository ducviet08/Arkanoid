// view/PauseScreen.java
package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image; // Thêm import này
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class PauseScreen {

    private Button continueButton;
    private Button exitToMenuButton;

    public PauseScreen() {
        continueButton = new Button("Continue");
        continueButton.setPrefSize(200, 60);
        continueButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;");

        exitToMenuButton = new Button("Exit to Menu");
        exitToMenuButton.setPrefSize(200, 60);
        exitToMenuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;");
    }

    public Button getContinueButton() {
        return continueButton;
    }

    public Button getExitToMenuButton() {
        return exitToMenuButton;
    }

    public Scene getScene(Stage stage, int width, int height) {
        // --- Thêm phần code tải và thiết lập BackgroundImage ---
        Image img = new Image(getClass().getResource("/images/background.png").toExternalForm());
        BackgroundImage bg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        // --------------------------------------------------------

        Label pausedLabel = new Label("⏸ Game Paused");
        pausedLabel.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        VBox buttonLayout = new VBox(20);
        buttonLayout.getChildren().addAll(pausedLabel, continueButton, exitToMenuButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setMaxWidth(Region.USE_PREF_SIZE);
        buttonLayout.setMaxHeight(Region.USE_PREF_SIZE);
        buttonLayout.setPadding(new Insets(0, 0, 0, 0));

        StackPane root = new StackPane(buttonLayout);
        root.setBackground(new Background(bg)); // Gắn background vào root StackPane
        // root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);"); // Bỏ hoặc thay đổi màu mờ nếu muốn

        Scene scene = new Scene(root, width, height);
        return scene;
    }
}