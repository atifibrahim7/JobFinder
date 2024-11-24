package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

public class AddVacancyController {
    @FXML private TextArea detailsField;
    @FXML private TextArea requirementsField;
    @FXML private TextField locationField;
    @FXML private DatePicker deadlinePicker;
    @FXML private Button dashboardBtn;
    @FXML private Button profileBtn;
    @FXML private Button settingsBtn;
    @FXML private Button logoutBtn;
    @FXML private Button submitBtn;
    @FXML private Button cancelBtn;

    private Recruiter currentRecruiter;
    private Company company;

    public void initialize() {
        // Initialize navigation button handlers
        setupNavigationHandlers();
    }

    public void setRecruiter(Recruiter recruiter) {
        this.currentRecruiter = recruiter;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @FXML
    private void handleSubmit() {
        if (validateInputs()) {
            job_vacancy newVacancy = createVacancy();
            // Add to database or storage
            navigateToDashboard();
        }
    }

    @FXML
    private void handleCancel() {
        navigateToDashboard();
    }

    private boolean validateInputs() {
        if (detailsField.getText().isEmpty() || 
            requirementsField.getText().isEmpty() || 
            locationField.getText().isEmpty() || 
            deadlinePicker.getValue() == null) {
            showAlert("Error", "All fields must be filled.");
            return false;
        }

        if (deadlinePicker.getValue().isBefore(LocalDate.now())) {
            showAlert("Error", "Deadline cannot be in the past.");
            return false;
        }

        return true;
    }

    private job_vacancy createVacancy() {
        job_vacancy vacancy = new job_vacancy();
        vacancy.company = this.company;
        vacancy.details = detailsField.getText();
        vacancy.requirements = requirementsField.getText();
        vacancy.location = locationField.getText();
        vacancy.date_posted = LocalDate.now().toString();
        vacancy.deadline = deadlinePicker.getValue().toString();
        vacancy.recruiter = this.currentRecruiter;
        //vacancy.applications = new ArrayList<>();
        return vacancy;
    }

    private void setupNavigationHandlers() {
        dashboardBtn.setOnAction(e -> navigateToDashboard());
        profileBtn.setOnAction(e -> navigateToProfile());
        settingsBtn.setOnAction(e -> navigateToSettings());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployerDashboard.fxml"));
            Parent root = loader.load();
            ManageVacanciesController controller = loader.getController();
            controller.setRecruiter(currentRecruiter);
            controller.setCompany(company);
            
            Stage stage = (Stage) dashboardBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
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