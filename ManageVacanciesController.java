package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

public class ManageVacanciesController {
    @FXML private TableView<job_vacancy> vacanciesTable;
    @FXML private TableColumn<job_vacancy, String> idColumn;
    @FXML private TableColumn<job_vacancy, String> detailsColumn;
    @FXML private TableColumn<job_vacancy, String> requirementsColumn;
    @FXML private TableColumn<job_vacancy, String> locationColumn;
    @FXML private TableColumn<job_vacancy, String> datePostedColumn;
    @FXML private TableColumn<job_vacancy, String> deadlineColumn;
    
    @FXML private TextField searchField;
    @FXML private Button deleteBtn;
    @FXML private Button addVacancyBtn;
    @FXML private Button dashboardBtn;
    @FXML private Button profileBtn;
    @FXML private Button settingsBtn;
    @FXML private Button logoutBtn;

    private Recruiter currentRecruiter;
    private Company company;
    private ObservableList<job_vacancy> vacanciesList = FXCollections.observableArrayList();

    public void initialize() {
        setupTableColumns();
        setupNavigationHandlers();
        loadVacancies();
    }

    public void setRecruiter(Recruiter recruiter) {
        this.currentRecruiter = recruiter;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        requirementsColumn.setCellValueFactory(new PropertyValueFactory<>("requirements"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        datePostedColumn.setCellValueFactory(new PropertyValueFactory<>("date_posted"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
    }

    private void loadVacancies() {
        // Load vacancies from database or storage
        vacanciesTable.setItems(vacanciesList);
        
        //display the query information for 
        
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
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        String searchId = searchField.getText();
        if (searchId.isEmpty()) {
            showAlert("Error", "Please enter a vacancy ID to delete.");
            return;
        }

        // Implementation for deletion logic
        // Remove from database and refresh table
        loadVacancies();
    }

    private void setupNavigationHandlers() {
        dashboardBtn.setOnAction(e -> refreshDashboard());
        profileBtn.setOnAction(e -> navigateToProfile());
        settingsBtn.setOnAction(e -> navigateToSettings());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void refreshDashboard() {
        loadVacancies();
    }

    private void navigateToProfile() {
        // Implementation for profile navigation
    }

    private void navigateToSettings() {
        // Implementation for settings navigation
    }

    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
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