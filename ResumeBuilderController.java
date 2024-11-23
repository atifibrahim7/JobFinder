package application;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

public class ResumeBuilderController {

    @FXML
    private TextArea educationField;

    @FXML
    private TextArea experienceField;

    @FXML
    private TextArea skillsField;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> handleSubmit());
    }

    private void handleSubmit() {
        String education = educationField.getText();
        String experience = experienceField.getText();
        String skills = skillsField.getText();
        
        // Call backend to save data (example)
        Controller.db.addResume(UserSession.currentUsername, education, experience, skills);

        // After saving, redirect to the profile page
        redirectToProfilePage();
    }

    private void redirectToProfilePage() {
    	  try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/account1.fxml"));
              HBox root = loader.load();  // Load the FXML

              Scene scene = new Scene(root);
              Stage stage = (Stage) educationField.getScene().getWindow();
              stage.setScene(scene);  // Change the scene to the next page
          } catch (Exception e) {
              e.printStackTrace();
              System.out.println("Error loading the next page.");
          }
    }
}