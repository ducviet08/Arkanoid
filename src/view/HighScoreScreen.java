package view;

import Arkanoid.Main;
import controller.HighScoreManager;
import controller.ScoreInput;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class HighScoreScreen {

    private HighScoreManager highScoreManager;
    private VBox scoresContainer;

    public HighScoreScreen() {
        this.highScoreManager = new HighScoreManager();
    }

    private VBox createScoresList() {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        List<ScoreInput> scores = highScoreManager.getHighScores();

        if (scores.isEmpty()) {
            Label noScoreLabel = new Label("NO HIGH SCORES YET. PLAY THE GAME!");
            noScoreLabel.setFont(Font.font("Arial", 24));
            noScoreLabel.setTextFill(Color.WHITE);
            container.getChildren().add(noScoreLabel);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String name = scores.get(i).getPlayerName();
                int score = scores.get(i).getPlayerScore();
                Label scoreLabel = new Label(String.format("%02d. %s %d", i + 1, name, score));
                scoreLabel.setFont(Font.font("Arial", 28));
                scoreLabel.setTextFill(Color.WHITE);
                container.getChildren().add(scoreLabel);
            }
        }
        return container;
    }

    public Scene getScene(Stage stage, Main main) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(50));
        root.setBackground(Background.fill(Color.web("#333333")));

        Label title = new Label("HIGH SCORES");
        title.setFont(Font.font("Arial", 60));
        title.setTextFill(Color.web("#FF4500"));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        scoresContainer = createScoresList();
        root.setCenter(scoresContainer);

        HBox bottomBox = new HBox(30);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(30, 0, 0, 0));

        // Nút BACK TO MENU (Quay lại StartScreen/Menu chính)
        Button backButton = new Button("BACK TO MENU");
        backButton.setStyle("-fx-font-size: 24; -fx-background-color: lightblue; -fx-text-fill: black; -fx-padding: 10 20; -fx-background-radius: 10;");
        backButton.setOnAction(e -> {
            // Quay lại StartScreen, vì Main đang sử dụng StartScreen
            StartScreen startScreen = new StartScreen();
            stage.setScene(startScreen.getScene(stage, main));
        });

        // Nút CLEAR HIGH SCORES
        Button clearButton = new Button("CLEAR HIGH SCORES");
        clearButton.setStyle("-fx-font-size: 24; -fx-background-color: #DC143C; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
        clearButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Clear All High Scores?");
            alert.setContentText("Are you sure you want to delete all recorded high scores? This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                highScoreManager.deleteAllDataHighScore();
                highScoreManager.saveHighScore();

                // Cập nhật lại giao diện
                root.setCenter(createScoresList());
            }
        });

        bottomBox.getChildren().addAll(backButton, clearButton);
        root.setBottom(bottomBox);

        return new Scene(root, 800, 600);
    }
}