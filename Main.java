package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import java.sql.*;
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	HBox   root = FXMLLoader.load(getClass().getResource("account1.fxml"));	
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Page");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	Controller c = new Controller() ; 
 
    	
    	c.controller_start();
        launch(args);
    }
    
}
