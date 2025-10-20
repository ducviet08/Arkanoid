package view;

import javafx.geometry.Insets; // Import lớp Insets
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class StartScreen {

    private Button startGameButton;
    private Button exitButton;

    public StartScreen() {
        startGameButton = new Button("Start Game");
        startGameButton.setPrefSize(200, 60);
        startGameButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;");

        exitButton = new Button("Exit");
        exitButton.setPrefSize(200, 60);
        exitButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;");
    }

    public Button getStartGameButton() {
        return startGameButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    public Scene getScene(Stage stage, int width, int height) {
        Image img = new Image(getClass().getResource("/images/BG.jpg").toExternalForm());
        BackgroundImage bg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        VBox buttonLayout = new VBox(20); // Khoảng cách giữa các nút
        buttonLayout.getChildren().addAll(startGameButton, exitButton);
        buttonLayout.setAlignment(javafx.geometry.Pos.CENTER);
        buttonLayout.setMaxWidth(Region.USE_PREF_SIZE);
        buttonLayout.setMaxHeight(Region.USE_PREF_SIZE);

        // THÊM DÒNG NÀY ĐỂ ĐẨY NÚT XUỐNG DƯỚI
        buttonLayout.setPadding(new Insets(150, 0, 0, 0)); // Đẩy xuống 150 pixels từ phía trên (top)

        StackPane root = new StackPane();
        root.setBackground(new Background(bg));
        root.getChildren().add(buttonLayout);

        Scene scene = new Scene(root, width, height);
        return scene;
    }
}