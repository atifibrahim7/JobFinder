package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class RecruiterProfileController {

    @FXML
    private Button profileBtn;

    @FXML
    private Button viewCompaniesBtn;

    @FXML
    private Button manageApplicationBtn;

    @FXML
    private Button logoutBtn;

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
    private Label educationLabel;

    @FXML
    private Label experienceLabel;

    @FXML
    private Label skillsLabel;

    @FXML
    private Label companyLabel;

    @FXML
    private VBox recruiterInfoBox;

    @FXML
    public void initialize() {
        setupButtonHandlers();
        loadProfileData();
    }

    private void setupButtonHandlers() {
        profileBtn.setOnAction(e -> handleProfile());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        manageApplicationBtn.setOnAction(e -> handleManageApplication());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void loadProfileData() {
        try {
            ArrayList<String> user_info = Controller.db.getUser(UserSession.currentUsername, UserSession.currentRole);
            ArrayList<String> resume_info;

            if (user_info != null && !user_info.isEmpty()) {
                String username = user_info.get(0);
                String name = user_info.get(1);
                String email = user_info.get(2);
                String company = "Company";

                String educationHistory = "No Current History";
                String pastExperience = "No Current History";
                String skills = "No Current Skills";

                nameLabel.setText("Name: " + name);
                emailLabel.setText("Email: " + email);
                positionLabel.setText("Username: " + username);

                if(Controller.db.isResume(UserSession.currentUsername)) {
                    resume_info = Controller.db.getResume(UserSession.currentUsername);
                    educationHistory = resume_info.get(1);
                    pastExperience = resume_info.get(2);
                    skills = resume_info.get(3);
                }

                educationLabel.setText(educationHistory);
                experienceLabel.setText(pastExperience);
                skillsLabel.setText(skills);


                companyLabel.setText(company);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleProfile() {
        System.out.println("Profile clicked");
    }

    private void handleViewCompanies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/ViewCompanies.fxml"));
            Scene viewCompaniesScene = new Scene(loader.load());
            Stage stage = (Stage) viewCompaniesBtn.getScene().getWindow();
            stage.setScene(viewCompaniesScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleManageApplication() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/recruiterApplication.fxml"));
            Scene manageVacanciesScene = new Scene(loader.load());
            Stage stage = (Stage) manageApplicationBtn.getScene().getWindow();
            stage.setScene(manageVacanciesScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}