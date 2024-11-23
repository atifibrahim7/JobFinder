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
        	HBox   root = FXMLLoader.load(getClass().getResource("registerJH.fxml"));
            Scene scene = new Scene(root, 800, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Page");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() 
    {
    	try {
    		
    		String URL = "jdbc:postgresql://localhost:5432/postgres";
    		String USER = "postgres";
    		String PASSWORD = "12345678";
    		// Load the PostgreSQL driver (optional in modern versions)
    		Class.forName("org.postgresql.Driver");
    		return DriverManager.getConnection(URL, USER, PASSWORD);
    	} catch (ClassNotFoundException e) {
    		System.out.println("PostgreSQL JDBC Driver not found.");
    		e.printStackTrace();
    		return null;
    	} catch (SQLException e) {
    		System.out.println("Failed to connect to PostgreSQL database.");
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public static void main(String[] args) {
    	// Main.getConnection();
    	Controller c = new Controller() ; 
    	
    	c.controller_start();
        launch(args);
    }
    
}
