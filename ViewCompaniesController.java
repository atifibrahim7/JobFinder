package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ViewCompaniesController {

    @FXML
    private Button jobVacanciesBtn;
    @FXML
    private Button viewCompaniesBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private Button pendingApplicationsBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private TextField searchField;
    @FXML
    private VBox companiesContainer;

    @FXML
    public void initialize() {
        setupButtonHandlers();
        loadCompanies();
        setupSearch();
    }

    private void setupButtonHandlers() {
        jobVacanciesBtn.setOnAction(e -> handleJobVacancies());
        profileBtn.setOnAction(e -> handleProfile());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
        pendingApplicationsBtn.setOnAction(e -> handlePendingApplications());
    }

    private void loadCompanies() {
        try {
            ArrayList<ArrayList<String>> companies = Controller.db.getAllCompanies();
            for (ArrayList<String> company : companies) {
                VBox companyBox = createCompanyBox(company);
                companiesContainer.getChildren().add(companyBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createCompanyBox(ArrayList<String> company) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-color: #cccccc; " +
                     "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        
        Label nameLabel = new Label(company.get(0));
        Label emailLabel = new Label("Email: " + company.get(1));
        Label descriptionLabel = new Label("Description: " + company.get(2));
        
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        box.getChildren().addAll(nameLabel, emailLabel, descriptionLabel);
        
        // Add mouse click event to the company box
        box.setOnMouseClicked(event -> handleCompanyClick(company.get(0))); // Pass the company name
        return box;
    }

    // Navigate to job vacancies for the selected company
    private void handleCompanyClick(String companyName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JobVacancies.fxml"));
            Scene jobVacanciesScene = new Scene(loader.load());
            JobVacanciesController controller = loader.getController();
            controller.setCompanyName(companyName); // Pass the selected company name
            Stage stage = (Stage) companiesContainer.getScene().getWindow();
            stage.setScene(jobVacanciesScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCompanies(newValue);
        });
    }

    private void filterCompanies(String searchText) {
        try {
            companiesContainer.getChildren().clear();
            ArrayList<ArrayList<String>> companies = Controller.db.getAllCompanies();
            for (ArrayList<String> company : companies) {
                if (company.get(0).toLowerCase().contains(searchText.toLowerCase()) ||
                    company.get(2).toLowerCase().contains(searchText.toLowerCase())) {
                    VBox companyBox = createCompanyBox(company);
                    companiesContainer.getChildren().add(companyBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleProfile() {
        String path = "account1.fxml";
        if ("Employer".equals(UserSession.currentRole)) {
            path = "employerDashboard.fxml";
        }
        if ("Recruiter".equals(UserSession.currentRole)) {
            path = "recruiteraccount.fxml";
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene profileScene = new Scene(loader.load());
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            stage.setScene(profileScene);
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

    private void handlePendingApplications() {
        System.out.println("Pending Applications clicked");
    }

    private void handleJobVacancies() {
        System.out.println("Job Vacancies clicked");
    }

    private void handleViewCompanies() {
        System.out.println("View Companies clicked");
    }
}