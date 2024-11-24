package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
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
    private Button pendingApplicationsBtn;  // New Button

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

    // Set up button handlers for navigation
    private void setupButtonHandlers() {
        jobVacanciesBtn.setOnAction(e -> handleJobVacancies());
        profileBtn.setOnAction(e -> handleProfile());
        viewCompaniesBtn.setOnAction(e -> handleViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
        pendingApplicationsBtn.setOnAction(e -> handlePendingApplications());  // Handler for Pending Applications

    }

    // Load all companies from the database and display them
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

    // Create a visual box to represent each company
    private VBox createCompanyBox(ArrayList<String> company) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-color: #cccccc; " +
                    "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label nameLabel = new Label(company.get(0));
        Label emailLabel = new Label("Email: " + company.get(1));
        Label descriptionLabel = new Label("Description: " + company.get(2));

        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        box.getChildren().addAll(nameLabel, emailLabel, descriptionLabel);
        return box;
    }

    // Set up search functionality
    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCompanies(newValue);
        });
    }

    // Filter companies based on the search input
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

    // Handle profile button click, navigate to the profile page
    private void handleProfile() {
        String path = "account1.fxml";
        if("Employer".equals(UserSession.currentRole)) {
        	path = "employerDashboard.fxml";
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

    // Handle logout button click, navigate to the login page
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

    
    private void handlePendingApplications()
    {
        System.out.println("pending clicked");

    }
    // Handle job vacancies button click, navigate to job vacancies page (currently no action)
    private void handleJobVacancies() {
        // Add navigation to job vacancies page if implemented
        System.out.println("Job Vacancies clicked");
    }

    // Handle view companies button click, but we are already on the companies page
    private void handleViewCompanies() {
        // No action needed, we are already on the companies page
        System.out.println("View Companies clicked");
    }
}
