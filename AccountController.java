package application;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountController {

	@FXML
	private Button jobVacanciesBtn;

	@FXML
	private Button viewCompaniesBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Button pendingApplicationsBtn;  // New Button

    @FXML
    private Button logoutBtn;

    @FXML
    private Button attachResumeBtn;

    @FXML
    private Rectangle banner;

    @FXML
    private Rectangle profilePicture;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private VBox resumeInfoBox;

    @FXML
    public void initialize() {
        setupButtonHandlers();
        loadProfileData();
    }

    private void setupButtonHandlers() {
        jobVacanciesBtn.setOnAction(e -> handleJobVacancies());
        profileBtn.setOnAction(e -> handleProfile());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        pendingApplicationsBtn.setOnAction(e -> handlePendingApplications());  // Handler for Pending Applications
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void loadProfileData() {
        try {
            // Connect to the database
            ArrayList<String> user_info = Controller.db.getUser(UserSession.currentUsername, UserSession.currentRole);
            ArrayList<String>resume_info;
            // Fetch user information
            if (user_info != null && !user_info.isEmpty()) {
                String username = user_info.get(0);  // Username
                String name = user_info.get(1);      // Name
                String email = user_info.get(2);	// Email
                String company = user_info.get(4);         // Company (new data)

                System.out.println(username + " " + name + " " + email);
                
                
                String educationHistory = "No Current History";  // Education History (new data)
                String pastExperience = "Not Current History";    // Past Experience (new data)
                String skills = "Not Currenty Skills";          // Skills (new data)
                
                
                // Populate user information
                nameLabel.setText("Name: " + name);
                emailLabel.setText("Email: " + email);
                positionLabel.setText("Username: " + username);
                
                // Populate resume info
                // Update your resume info labels with the data fetched from the backend
                if(Controller.db.isResume(UserSession.currentUsername))
                {
	                resume_info = Controller.db.getResume(UserSession.currentUsername);
	                educationHistory = resume_info.get(1);
	                pastExperience = resume_info.get(2);
	                skills = resume_info.get(3);
                }
                
                Label educationLabel = new Label("Education: " + educationHistory);
                Label experienceLabel = new Label("Experience: " + pastExperience);
                Label skillsLabel = new Label("Skills: " + skills);

                if(company == null)
                	company = "Not working for any";
                Label companyLabel = new Label("Company: " + company);
                resumeInfoBox.getChildren().clear();  // Clear previous contents (if any)
                resumeInfoBox.getChildren().addAll(educationLabel, experienceLabel, skillsLabel, companyLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleProfile() {
        System.out.println("Profile clicked");
    }

    private void handleLogout() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Dummy handler for "Pending Applications"
    private void handlePendingApplications() {
        System.out.println("Pending Applications clicked");
        // Implement functionality here
    }

    // Handler for Job Vacancies
    private void handleJobVacancies() {
        System.out.println("Job Vacancies clicked");
        // Implement functionality here
    }

    // Handler for View Companies
    private void handleViewCompanies() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewCompanies.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) viewCompaniesBtn.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}