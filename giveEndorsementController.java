package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class giveEndorsementController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextArea detailsField;
    @FXML
    private Button profileBtn;
    @FXML
    private Button manageVacanciesBtn;
    @FXML
    private Button viewCompaniesBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button ViewEndorsementsBtn11;
    
    private String jobHunterName;

    @FXML
    public void initialize() {
        setupButtonHandlers();
    }

    private void setupButtonHandlers() {
        profileBtn.setOnAction(e -> handleProfile());
        manageVacanciesBtn.setOnAction(e -> handleManageVacancies());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
        ViewEndorsementsBtn11.setOnAction(e -> goToViewEndorsements());
    }
    private void goToViewEndorsements() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewEndorsements.fxml"));
            Scene viewEndorsementsScene = new Scene(loader.load());
            Stage stage = (Stage) ViewEndorsementsBtn11.getScene().getWindow();
            stage.setScene(viewEndorsementsScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load endorsements page");
        }
    }

    public void setJobHunterName(String jobHunterName) {
        this.jobHunterName = jobHunterName;
        loadJobHunterData();
    }

    private void loadJobHunterData() {
        try {
            ArrayList<String> jobhunter_info = Controller.db.getUser(jobHunterName, "JobHunter");

            if (jobhunter_info != null && !jobhunter_info.isEmpty()) {
                String username = jobhunter_info.get(0);
                String name = jobhunter_info.get(1);
                String email = jobhunter_info.get(2);

                nameLabel.setText("Name: " + name);
                emailLabel.setText("Email: " + email);
                usernameLabel.setText("Username: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load job hunter data");
        }
    }

    private void handleProfile() {
        try {
            String path = "account1.fxml";
            if ("Employer".equals(UserSession.currentRole)) {
                path = "employerDashboard.fxml";
            }
            if ("Recruiter".equals(UserSession.currentRole)) {
                path = "recruiteraccount.fxml";
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene profileScene = new Scene(loader.load());
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            stage.setScene(profileScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load profile page");
        }
    }

    private void handleManageVacancies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageVacancies.fxml"));
            Scene manageVacanciesScene = new Scene(loader.load());
            Stage stage = (Stage) manageVacanciesBtn.getScene().getWindow();
            stage.setScene(manageVacanciesScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load manage vacancies page");
        }
    }

    private void handleViewCompanies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewCompanies.fxml"));
            Scene viewCompaniesScene = new Scene(loader.load());
            Stage stage = (Stage) viewCompaniesBtn.getScene().getWindow();
            stage.setScene(viewCompaniesScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load companies page");
        }
    }

    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout");
        }
    }

    @FXML
    private void handleSubmit() {
        if (detailsField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter endorsement details");
            return;
        }

        try {
            // Add code here to save the endorsement to the database
        	// Function call to save vacancy in database
        	 Controller.db.addEndorsement(
        	            jobHunterName,                  // Job hunter username
        	            UserSession.currentUsername,     // Current user (endorser) username
        	            detailsField.getText()          // Endorsement description
        	 );
            showAlert("Success", "Endorsement submitted successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to submit endorsement");
        }
    }

    @FXML
    private void handleCancel() {
    	try {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewJobHunters.fxml"));
        Scene loginScene = new Scene(loader.load());
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.setScene(loginScene);
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error", "Failed to logout");
    }
    }

    private void closeWindow() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert.AlertType type = title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}