package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class EmployerDashboardController {

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button addVacancyBtn;
    @FXML
    private Button manageVacanciesBtn;
    
    @FXML
    private Rectangle banner;
    @FXML
    private Rectangle profilePicture;
    
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label companyLabel;

    @FXML
    public void initialize() {
        loadEmployerData();
        setupButtonHandlers();
    }

    private void loadEmployerData() {
        try {
            ArrayList<String> employer_info = Controller.db.getUser(UserSession.currentUsername, UserSession.currentRole);
            
            if (employer_info != null && !employer_info.isEmpty()) {
                String username = employer_info.get(0);
                String name = employer_info.get(1);
                String email = employer_info.get(2);
                String company = employer_info.get(4);
                
                nameLabel.setText("Name: " + name);
                emailLabel.setText("Email: " + email);
                usernameLabel.setText("Username: " + username);
                companyLabel.setText("Company: " + (company != null ? company : "Not specified"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupButtonHandlers() {
        dashboardBtn.setOnAction(e -> handleDashboard());
        profileBtn.setOnAction(e -> handleProfile());
        settingsBtn.setOnAction(e -> handleSettings());
        logoutBtn.setOnAction(e -> handleLogout());
        addVacancyBtn.setOnAction(e -> handleAddVacancy());
        manageVacanciesBtn.setOnAction(e -> handleManageVacancies());
    }

    private void handleDashboard() {
        System.out.println("Dashboard clicked");
    }

    private void handleProfile() {
        System.out.println("Profile clicked");
    }

    private void handleSettings() {
        System.out.println("Settings clicked");
    }

    private void handleLogout() {
        System.out.println("Logout clicked");
    }

    private void handleAddVacancy() {
        try {
            HBox root = FXMLLoader.load(getClass().getResource("AddVacancy.fxml"));
            Scene scene = new Scene(root, 800, 800);
            Stage stage = (Stage) addVacancyBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Add Vacancy");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleManageVacancies() {
        try {
            HBox root = FXMLLoader.load(getClass().getResource("ManageVacancy.fxml"));
            Scene scene = new Scene(root, 800, 800);
            Stage stage = (Stage) manageVacanciesBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manage Vacancies");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}