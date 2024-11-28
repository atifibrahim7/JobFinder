package application;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class JobDetailsController {
    @FXML private Label titleLabel;
    @FXML private Label companyLabel;
    @FXML private Label locationLabel;
    @FXML private Label datePostedLabel;
    @FXML private Label deadlineLabel;
    @FXML private TextArea detailsArea;
    @FXML private TextArea requirementsArea;
    @FXML private Button applyButton;

    private String vacancyTitle;

    public void setJobDetails(String title, String company, String location,
                            Date datePosted, Date deadline, String details,
                            String requirements) {
        this.vacancyTitle = title;
        titleLabel.setText(title);
        companyLabel.setText(company);
        locationLabel.setText(location);
        datePostedLabel.setText(datePosted.toString());
        deadlineLabel.setText(deadline.toString());
        detailsArea.setText(details);
        requirementsArea.setText(requirements);

        // Disable apply button if user is not a job hunter or if deadline has passed
        if (Controller.Current_JH == null ||
            deadline.toLocalDate().isBefore(LocalDate.now())) {
            applyButton.setDisable(true);
        }
    }

    @FXML
    private void handleApply() {
        if (Controller.Current_JH != null) {
            try {
            	System.out.println("here");
                Controller.db.addApplication(
                    Controller.Current_JH.username,
                    vacancyTitle,
                    Date.valueOf(LocalDate.now())
                );
                showAlert("Success", "Application submitted successfully!");
                goBack();
            } catch (Exception e) {
                showAlert("Error", "Failed to submit application: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userInterface/ViewCompanies.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}