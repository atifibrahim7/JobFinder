package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JobhunterApplicationsController implements Initializable {
    @FXML private Button profileBtn;
    @FXML private Button pendingApplicationsBtn;
    @FXML private Button jobVacanciesBtn;
    @FXML private Button viewCompaniesBtn;
    @FXML private Button logoutBtn;
    @FXML private VBox applicationsContainer;

    private List<application> applications = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupButtonHandlers();
        loadApplications();
        displayApplications();
    }

    private void setupButtonHandlers() {
        profileBtn.setOnAction(e -> handleProfile());
        pendingApplicationsBtn.setOnAction(e -> handlePendingApplications());
        jobVacanciesBtn.setOnAction(e -> handleJobVacancies());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void loadApplications() {
        ArrayList<ArrayList<String>> applicationData = Controller.db.getJHApplications(UserSession.currentUsername);
        applications.clear();

        for (ArrayList<String> appData : applicationData) {
            application app = new application();
            app.companyName = appData.get(0);
            app.requirements = appData.get(1);
            app.vacancyTitle = appData.get(2);
            app.status = appData.get(3); 
            applications.add(app);
            System.out.println(app);
        }
    }

    private void displayApplications() {
        applicationsContainer.getChildren().clear();

        for (application app : applications) {
            VBox applicationBox = createApplicationBox(app);
            applicationsContainer.getChildren().add(applicationBox);
        }
    }

    private VBox createApplicationBox(application app) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5;");

        // Job Details
        Label companyLabel = new Label("Company: " + app.companyName);
        companyLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        Label vacancyLabel = new Label("Position: " + app.vacancyTitle);
        vacancyLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Status
        Label statusLabel = new Label("Status: " + app.status);
        statusLabel.setStyle("-fx-font-size: 14;");

        // Requirements
        VBox requirementsBox = new VBox(5);
        requirementsBox.setStyle("-fx-padding: 10; -fx-background-color: #f8f8f8;");
        Label requirementsLabel = new Label("Requirements: " + app.requirements);
        requirementsLabel.setWrapText(true);
        requirementsBox.getChildren().add(requirementsLabel);

        box.getChildren().addAll(
            companyLabel,
            vacancyLabel,
            statusLabel,
            requirementsBox
        );

        return box;
    }

    private void handleProfile() {
    	String path = "account1.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene profileScene = new Scene(loader.load());
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            stage.setScene(profileScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePendingApplications() {
        navigateTo("PendingApplications.fxml");
    }

    private void handleJobVacancies() {
        navigateTo("JobVacancies.fxml");
    }

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

    private void navigateTo(String fxmlFile) {
    	System.out.println("button pressed +" + fxmlFile);
    }
}
