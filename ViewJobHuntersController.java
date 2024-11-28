package application;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewJobHuntersController  {

    @FXML
    private Button profileBtn;
    @FXML
    private Button manageVacanciesBtn;
    @FXML
    private Button viewCompaniesBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private TextField searchField;
    @FXML
    private VBox jobHunterContainer; // Note: matches the FXML id exactly

    @FXML
    public void initialize() {
        setupButtonHandlers();
        loadJobHunters();
        setupSearch();
    }

    private void setupButtonHandlers() {
        profileBtn.setOnAction(e -> handleProfile());
        manageVacanciesBtn.setOnAction(e -> handleManageVacancies());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void loadJobHunters() {
        try {
            List<String> jobHunters = Controller.db.getAllJobHunters();
            jobHunterContainer.getChildren().clear();

            for (String jobHunterInfo : jobHunters) {
                VBox jobHunterBox = createJobHunterBox(jobHunterInfo);
                jobHunterContainer.getChildren().add(jobHunterBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load job hunters: " + e.getMessage());
        }
    }

    private VBox createJobHunterBox(String jobHunterInfo) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-color: #cccccc; " +
                     "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Parse the jobHunterInfo string
        String[] parts = jobHunterInfo.split(", ");
        String username = parts[0].replace("Username: ", "");
        String name = parts[1].replace("Name: ", "");

        Label nameLabel = new Label("Name: " + name);
        Label usernameLabel = new Label("Username: " + username);

        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        box.getChildren().addAll(nameLabel, usernameLabel);

        // Add hover effect
        box.setOnMouseEntered(e ->
            box.setStyle(box.getStyle() + "-fx-background-color: #f5f5f5;"));
        box.setOnMouseExited(e ->
            box.setStyle(box.getStyle() + "-fx-background-color: white;"));

        // Add click event
        box.setOnMouseClicked(event -> handleJobHunterClick(username));

        return box;
    }

    private void handleJobHunterClick(String jobHunterName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/giveEndorsement.fxml"));
            Scene jobHunterProfileScene = new Scene(loader.load());
            giveEndorsementController controller = loader.getController();
            controller.setJobHunterName(jobHunterName); // Pass the selected jobHunter name
            Stage stage = (Stage) jobHunterContainer.getScene().getWindow();
            stage.setScene(jobHunterProfileScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterJobHunters(newValue.toLowerCase());
        });
    }

    private void filterJobHunters(String searchText) {
        try {
            List<String> allJobHunters = Controller.db.getAllJobHunters();
            jobHunterContainer.getChildren().clear();

            for (String jobHunterInfo : allJobHunters) {
                if (jobHunterInfo.toLowerCase().contains(searchText)) {
                    VBox jobHunterBox = createJobHunterBox(jobHunterInfo);
                    jobHunterContainer.getChildren().add(jobHunterBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to filter job hunters: " + e.getMessage());
        }
    }

    private void handleProfile() {
        try {
            String path = "userInterface/account1.fxml";
            if ("Employer".equals(UserSession.currentRole)) {
                path = "userInterface/employerDashboard.fxml";
            }
            if ("Recruiter".equals(UserSession.currentRole)) {
                path = "userInterface/recruiteraccount.fxml";
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/ManageVacancies.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/ViewCompanies.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}