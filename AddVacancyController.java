package application;
import javafx.application.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddVacancyController {
    @FXML private TextArea detailsField;
    @FXML private TextArea requirementsField;
    @FXML private TextField locationField;
    @FXML private DatePicker deadlinePicker;
    
    // Updated sidebar buttons to match new FXML
    @FXML private Button profileBtn;
    @FXML private Button manageVacanciesBtn;
    @FXML private Button viewCompaniesBtn;
    @FXML private Button logoutBtn;
    @FXML private Button submitBtn;
    @FXML private Button cancelBtn;

    private Recruiter currentRecruiter;
    private Company company;
    public String companyString;
    private ObservableList<job_vacancy> vacanciesList = FXCollections.observableArrayList();

    public void initialize() {
        setupNavigationHandlers();
        loadEmployerData();
    }
    private void loadEmployerData() {
        try {
            ArrayList<String> employer_info = Controller.db.getUser(UserSession.currentUsername, UserSession.currentRole);
            
            if (employer_info != null && !employer_info.isEmpty()) {

                String companyString = employer_info.get(4);
                
               
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


    @FXML
    private void handleSubmit() {
        if (validateInputs()) {
            job_vacancy newVacancy = createVacancy();
            vacanciesList.add(newVacancy);
            
            // Function call to save vacancy in database
            Controller.db.addVacancy(
                companyString,
                detailsField.getText(),
                requirementsField.getText(),
                locationField.getText(),
                LocalDate.now().toString(),
                deadlinePicker.getValue().toString(),
                UserSession.currentUsername
            );
            navigateToProfile();
        }
    }
    

    @FXML
    private void handleCancel() {
        navigateToManageVacancies();
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
        
        // Set vacancy properties
        vacancy.company = this.company;
        vacancy.details = detailsField.getText();
        vacancy.requirements = requirementsField.getText();
        vacancy.location = locationField.getText();
        vacancy.date_posted = LocalDate.now().toString();
        vacancy.deadline = deadlinePicker.getValue().toString();
        vacancy.recruiter = this.currentRecruiter;

        // Print all the details of the vacancy
        System.out.println("Job Vacancy Details:");
        System.out.println("Company: " + companyString);
        System.out.println("Details: " + vacancy.details);
        System.out.println("Requirements: " + vacancy.requirements);
        System.out.println("Location: " + vacancy.location);
        System.out.println("Date Posted: " + vacancy.date_posted);
        System.out.println("Deadline: " + vacancy.deadline);
        System.out.println("Recruiter: " + UserSession.currentUsername);

        return vacancy;
    }


    private void setupNavigationHandlers() {
        profileBtn.setOnAction(e -> navigateToProfile());
        manageVacanciesBtn.setOnAction(e -> navigateToManageVacancies());
        viewCompaniesBtn.setOnAction(e -> navigateToViewCompanies());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void navigateToManageVacancies() {
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

    private void navigateToProfile() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("employerDashboard.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) viewCompaniesBtn.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToViewCompanies() {
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
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}