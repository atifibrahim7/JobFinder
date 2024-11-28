package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            HBox root = FXMLLoader.load(getClass().getResource("userInterface/login.fxml"));
            Scene scene = new Scene(root, 800, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Page");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // hello
        }
    }

    public static void main(String[] args) {
        Controller c = new Controller();

        launch(args);
    }

}
