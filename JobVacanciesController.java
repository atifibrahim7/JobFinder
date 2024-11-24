package application;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;

public class JobVacanciesController {

    @FXML
    private ListView<String> jobVacanciesListView;

    @FXML
    private Label companyLabel;

    @FXML
    public void initialize() {
        // Any initialization if needed
    }

    public void setCompanyName(String companyName) {
        companyLabel.setText("Job Vacancies for: " + companyName);
        loadJobVacancies(companyName);
    }

    private void loadJobVacancies(String companyName) {
        List<String> jobVacancies = Controller.db.getJobVacanciesByCompany(companyName);
        jobVacanciesListView.getItems().addAll(jobVacancies);
    }

    @FXML
    private void goBack() {
    	try {
    		System.out.println("Controller.goBack()");
    		 FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewCompanies.fxml"));
             Scene accountScene = new Scene(loader.load());
             Stage stage = (Stage) jobVacanciesListView.getScene().getWindow();
             stage.setScene(accountScene);
             //Controller.db.test();
    		
    	} catch (IOException e ) {
    		e.printStackTrace();
    		   //Controller.db.test();
    	} 	
    }
}