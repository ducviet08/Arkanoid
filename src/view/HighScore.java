package view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class HighScore {

    // 👉 nếu file nằm trong src/data/
    private final String FILE_PATH = System.getProperty("user.dir") + "/src/data/score.txt";

    public Scene getScene(Stage stage, int playerScore) {
        Pane root = new Pane();

        // Ảnh nền
        Image bg = new Image("/images/HighScore.jpg");
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(800);
        bgView.setFitHeight(800);
        root.getChildren().add(bgView);

        // Mảng lưu label
        List<Label> sttList = new ArrayList<>();
        List<Label> nameList = new ArrayList<>();
        List<Label> scoreList = new ArrayList<>();

        // Tạo 9 dòng (STT, Name, Score)
        int startY = 280;
        int stepY = 50;
        for (int i = 0; i < 9; i++) {
            Label stt = new Label(String.valueOf(i + 1));
            stt.setLayoutX(150);
            stt.setLayoutY(startY + i * stepY);
            stt.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

            Label name = new Label("-----");
            name.setLayoutX(340);
            name.setLayoutY(startY + i * stepY);
            name.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

            Label score = new Label("0");
            score.setLayoutX(570);
            score.setLayoutY(startY + i * stepY);
            score.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

            sttList.add(stt);
            nameList.add(name);
            scoreList.add(score);

            root.getChildren().addAll(stt, name, score);
        }

        // Cập nhật bảng khi mở
        updateBoard(nameList, scoreList);

        return new Scene(root, 800, 800);
    }

    // 🔹 Cập nhật bảng điểm từ file
    private void updateBoard(List<Label> nameList, List<Label> scoreList) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("⚠️ File không tồn tại: " + FILE_PATH);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<String[]> entries = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) entries.add(parts);
            }

            // Sắp xếp giảm dần theo điểm
            entries.sort((a, b) ->
                    Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1]))
            );

            // Gán vào bảng
            for (int i = 0; i < nameList.size(); i++) {
                if (i < entries.size()) {
                    nameList.get(i).setText(entries.get(i)[0]);
                    scoreList.get(i).setText(entries.get(i)[1]);
                } else {
                    nameList.get(i).setText("-----");
                    scoreList.get(i).setText("0");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
