package Arkanoid.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class  testScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        StartScreen startScreen = new StartScreen();
        startScreen.show(primaryStage);  // gọi màn hình start
    }

    public static void main(String[] args) {
        launch(args);
    }
}
