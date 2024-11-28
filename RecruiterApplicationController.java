





package application;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class RecruiterApplicationController implements Initializable {
   @FXML private Button profileBtn;
   @FXML private Button pendingApplicationsBtn;
   @FXML private Button jobVacanciesBtn;
   @FXML private Button viewCompaniesBtn;
   @FXML private Button logoutBtn;
   @FXML private VBox applicationsContainer;
   private List<application> applications = new ArrayList<>();
   private job_vacancy currentVacancy;
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
       ArrayList<ArrayList<String>> applicationData = Controller.db.getApplications(UserSession.currentUsername);
       applications.clear();

       for (ArrayList<String> appData : applicationData) {
           application app = new application();
           app.companyName = appData.get(0);
           app.requirements = appData.get(1);
           app.vacancyTitle = appData.get(2);
           app.username = appData.get(3);
           applications.add(app);
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
       // Applicant Details
       Label applicantLabel = new Label("Applicant Username: " + app.username);

       // Requirements
       VBox requirementsBox = new VBox(5);
       requirementsBox.setStyle("-fx-padding: 10; -fx-background-color: #f8f8f8;");
       Label requirementsLabel = new Label("Requirements: " + app.requirements);
       requirementsLabel.setWrapText(true);
       requirementsBox.getChildren().add(requirementsLabel);
       // Buttons
       HBox buttonBox = new HBox(10);
       Button approveBtn = new Button("Approve");
       Button disapproveBtn = new Button("Disapprove");
       approveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
       disapproveBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
       approveBtn.setOnAction(e -> handleApprove(app));
       disapproveBtn.setOnAction(e -> handleDisapprove(app));
       buttonBox.getChildren().addAll(approveBtn, disapproveBtn);
       box.getChildren().addAll(
           companyLabel,
           vacancyLabel,
           applicantLabel,
           requirementsBox,
           buttonBox
       );
       return box;
   }
   private void handleApprove(application app) {
       showAlert("Approved", "Application for " + app.vacancyTitle + " from " + app.username + " has been approved.");
       app.update_status("approved");
   }
   private void handleDisapprove(application app) {
       showAlert("Disapproved", "Application for " + app.vacancyTitle + " from " + app.username + " has been disapproved.");
       app.update_status("disapproved");
   }
   private void showAlert(String title, String content) {
       Alert alert = new Alert(AlertType.INFORMATION);
       alert.setTitle(title);
       alert.setContentText(content);
       alert.showAndWait();
   }
   // Navigation handlers
   private void handleProfile() {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/recruiteraccount.fxml"));
           Scene accountScene = new Scene(loader.load());
           Stage stage = (Stage) profileBtn.getScene().getWindow();
           stage.setScene(accountScene);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   private void handlePendingApplications() {
       // TODO: Implement navigation to pending applications
   }
   private void handleJobVacancies() {
       // TODO: Implement navigation to job vacancies
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
