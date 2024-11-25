package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewEndorsementsController {

    @FXML
    private Button profileBtn, manageVacanciesBtn, viewCompaniesBtn, logoutBtn, backBtn;

    @FXML
    private TextArea endorsementsTextArea;

    @FXML
    public void initialize() {
        // Load endorsements data into the TextArea
        loadEndorsements();

        // Set up button handlers
        setupButtonHandlers();
    }

    private void loadEndorsements() {
        System.out.println("Loading endorsements...");

        // Replace with the actual endorser username you want to filter by
        String endorserUsername = UserSession.currentUsername; // You might want to set this dynamically

        try {
            ArrayList<Endorsement> endorsements = Controller.db.getEndorsementsByEndorser(endorserUsername);
            StringBuilder endorsementsText = new StringBuilder();

            for (Endorsement endorsement : endorsements) {
                endorsementsText.append(String.format("%s: %s (Date: %s)\n", 
                    endorsement.getJobHunterUsername(), 
                    endorsement.getDescription(), 
                    endorsement.getDate()));
            }

            endorsementsTextArea.setText(endorsementsText.toString());
        } catch (Exception e) {
            e.printStackTrace();
            endorsementsTextArea.setText("Failed to load endorsements: " + e.getMessage());
        }
    }
   

    private void setupButtonHandlers() {
        profileBtn.setOnAction(e -> handleProfile());
        manageVacanciesBtn.setOnAction(e -> handleManageVacancies());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
        backBtn.setOnAction(e -> handleBack());
    }

    private void handleProfile() {
        navigateTo("EmployerDashboard.fxml");
    }

    private void handleManageVacancies() {
    	navigateTo("EmployerDashboard.fxml");
    }

    private void handleViewCompanies() {
    	navigateTo("EmployerDashboard.fxml");
    }

    private void handleLogout() {
    	navigateTo("EmployerDashboard.fxml");;
    }

    private void handleBack() {
    	navigateTo("EmployerDashboard.fxml"); // Replace with the actual previous scene's FXML
    }

    private void navigateTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}