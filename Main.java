import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Tạo 1 label đơn giản
        Label label = new Label("JavaFX is working!");

        // Tạo layout
        StackPane root = new StackPane(label);

        // Tạo scene với kích thước 400x200
        Scene scene = new Scene(root, 400, 200);

        // Thiết lập stage
        primaryStage.setTitle("JavaFX Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Kiểm tra Java VM và phiên bản JavaFX
        System.out.println("Java VM: " + System.getProperty("java.vm.name"));
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("JavaFX version: " + System.getProperty("javafx.version")); // null nếu chưa load JavaFX

        // Khởi chạy ứng dụng JavaFX
        launch(args);
    }
}
