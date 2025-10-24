//package view;
//
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//import javafx.geometry.Pos;
//
//public class MenuScreen {
//    public Scene getScene(Stage stage) {
//        Image img = new Image(getClass().getResource("BG.jpg").toExternalForm());
//        BackgroundImage bg = new BackgroundImage(
//                img,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.CENTER,
//                new BackgroundSize(100, 100, true, true, true, false)
//        );
//        Button chooseStage = new Button("chooseStage");
//        Button tutorial = new Button("tutorial");
//        Button back = new Button("back");
//
//
//        VBox root = new VBox(20, chooseStage, tutorial, back);
//        root.setBackground(new Background(bg));
//        root.setAlignment(Pos.CENTER);
//
//        back.setOnAction(e -> {
//            StartScreen startScreen = new StartScreen();
//            stage.setScene(startScreen.getScene(stage));
//        });
//
//        chooseStage.setOnAction(e -> {
//            EndScreen endScreen = new EndScreen();
//            stage.setScene(endScreen.getScene(stage));
//        });
//
//        Scene scene = new Scene(root, 800, 600);
//        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
//        stage.setScene(scene);
//        stage.show();
//        return scene;
//    }
//}
