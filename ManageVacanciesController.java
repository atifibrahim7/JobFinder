package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageVacanciesController {
    @FXML private TextField vacancyTitleField;
    @FXML private TextField searchField1;
    @FXML private Button deleteBtn;
    @FXML private Button addVacancyBtn;
    @FXML private Button profileBtn;
    @FXML private Button manageVacanciesBtn;
    @FXML private Button viewCompaniesBtn;
    @FXML private Button logoutBtn;
    @FXML private VBox jobHunterContainer;

    private Recruiter currentRecruiter;
    private Company company;
    public String companyString;
    @FXML
    public void initialize() {
        setupButtonHandlers();
        loadVacancies();
        setupSearch();
        loadCompany();
    }
    private void loadCompany() {
        try {
            ArrayList<String> employer_info = Controller.db.getUser(UserSession.currentUsername, UserSession.currentRole);
            
            if (employer_info != null && !employer_info.isEmpty()) {
            	
                 companyString = employer_info.get(4);


                System.out.println("fetched company Details:");
                System.out.println("Company: " + companyString);
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setRecruiter(Recruiter recruiter) {
        this.currentRecruiter = recruiter;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private void setupButtonHandlers() {
        profileBtn.setOnAction(e -> navigateToProfile());
        manageVacanciesBtn.setOnAction(e -> loadVacancies());
        viewCompaniesBtn.setOnAction(e -> navigateToViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
        deleteBtn.setOnAction(e -> handleDelete());
    }


	private void loadVacancies() {
	    try {
	        List<String> vacancies = Controller.db.getVacanciesbyCompany( companyString);
	        jobHunterContainer.getChildren().clear();
	        
	        for (String vacancyInfo : vacancies) {
	            VBox vacancyBox = createVacancyBox(vacancyInfo);
	            jobHunterContainer.getChildren().add(vacancyBox);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        showAlert("Error", "Failed to load vacancies: " + e.getMessage());
	    }
	}


	private VBox createVacancyBox(String vacancyInfo) {
	    VBox box = new VBox(10);
	    box.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-color: #cccccc; " +
	                 "-fx-border-radius: 5px; -fx-background-radius: 5px;");
	    
	    String[] lines = vacancyInfo.split("\n");
	    for (String line : lines) {
	        Label label = new Label(line);
	        if (line.startsWith("Title:")) {
	            label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
	        }
	        box.getChildren().add(label);
	    }
	    
	    // Add hover effect
	    box.setOnMouseEntered(e -> 
	        box.setStyle(box.getStyle() + "-fx-background-color: #f5f5f5;"));
	    box.setOnMouseExited(e -> 
	        box.setStyle(box.getStyle() + "-fx-background-color: white;"));
	    
	    // Add click event
	    box.setOnMouseClicked(event -> handleVacancyClick(lines[0].substring(7))); // Get title from first line
	    
	    return box;
	}

    

    private void setupSearch() {
        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            filterVacancies(newValue.toLowerCase());
        });
    }

    private void handleVacancyClick(String title) {
        vacancyTitleField.setText(title);
    }

    private void filterVacancies(String searchText) {
        try {
            List<String> allVacancies = Controller.db.getVacanciesbyCompany(companyString);
            jobHunterContainer.getChildren().clear();
            
            for (String vacancyInfo : allVacancies) {
                if (vacancyInfo.toLowerCase().contains(searchText)) {
                    VBox vacancyBox = createVacancyBox(vacancyInfo);
                    jobHunterContainer.getChildren().add(vacancyBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to filter vacancies: " + e.getMessage());
        }
    }
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleAddVacancy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddVacancy.fxml"));
            Parent root = loader.load();
            AddVacancyController controller = loader.getController();
            controller.setRecruiter(currentRecruiter);
            controller.setCompany(company);
            
            Stage stage = (Stage) addVacancyBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Failed to open Add Vacancy window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        String title = vacancyTitleField.getText();
        if (title.isEmpty()) {
            showAlert("Error", "Please select a vacancy to delete.");
            return;
        }

        // Check if vacancy exists
        if (!Controller.db.isVacancyExists(title, companyString)) {
            showAlert("Error", "Vacancy not found.");
            return;
        }

        // Show confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Vacancy");
        confirmDialog.setContentText("Are you sure you want to delete the vacancy: " + title + "?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Proceed with deletion
                if (Controller.db.deleteVacancy(title, companyString)) {
                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Vacancy deleted successfully!");
                    successAlert.showAndWait();

                    // Clear the title field
                    vacancyTitleField.clear();

                    // Reload the vacancies
                    loadVacancies();
                } else {
                    showAlert("Error", "Failed to delete vacancy.");
                }
            }
        });
    }

    private void navigateToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployerDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Failed to navigate to Profile.");
            e.printStackTrace();
        }
    }

    private void navigateToViewCompanies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewCompanies.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewCompaniesBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Failed to navigate to View Companies.");
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Failed to logout.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}