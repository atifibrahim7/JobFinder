package application;

import java.time.chrono.AbstractChronology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.*;

class Resume{
    protected String education;
    protected String past_experience;
    protected String skills;

    public Resume()
    {

    }
    public Resume(String education, String past_experience, String skills){
        this.education = education;
        this.past_experience = past_experience;
        this.skills = skills;
    }
    public void display_resume(){
        System.out.println("Education: " + education);
        System.out.println("Experience: " + past_experience);
        System.out.println("Skills: " + skills);
    }
}

class Company
{
    protected String C_name;
    protected String address;
    protected String email;
    public Company(){}
    public Company(String C_name, String address, String email){
        this.C_name = C_name;
        this.address = address;
        this.email = email;
    }
}

abstract class profile{
    protected String name;
    protected String username;
    protected String email;
    protected String password;
}

class job_hunter extends profile{
    protected Resume resume;
    public Company company;
    public job_hunter(){}
    public job_hunter(String name,String username,String email,String password){
        this.name=name;
        this.username=username;
        this.email=email;
        this.password=password;
    }
    public void add_company(Company c)
    {
        company=c;
    }
    public void attach_resume(Resume r){
        resume=r;
    }
}
class employer extends profile{
    Company company;
}

class Recruiter extends profile{
    public Recruiter(String name,String username,String email,String password){
    this.name=name;
    this.username=username;
    this.email=email;
    this.password=password;
    }
}

class application
{
    job_hunter applicant;
    String date;
}

class job_vacancy
{
    Company company;
    String details;
    String requirements;
    String location;
    String date_posted;
    String deadline;
    ArrayList<application> applications;
    Recruiter recruiter;
}



public class Controller {
	
	
	
	@FXML
    private TextField usernameField; // For username input

    @FXML
    private PasswordField passwordField; // For password input

    
	
    static DBHandler db = new DBHandler();
    static String curr_user;
    static String curr_type;
    public void controller_start() {
    	db.test();
        System.out.println("Hello world!");
    }
    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (db.isProfile(username)) {
            Map<String, String> user = db.verify(username, password);
            if (user != null) {
                curr_user = user.get("username");
                curr_type = user.get("type");
                System.out.println("Login successful! Welcome, " + curr_user + " [" + curr_type + "]");
                UserSession.currentUsername = curr_user;
                UserSession.currentRole = curr_type ; 
                System.out.println(  UserSession.currentRole );
                if("JobHunter".equals(UserSession.currentRole)) {
                	goToAccount();
                	
                }
                else {
                	//System.out.println("Else login ");
                //	goToAccount();
                }
                
                
            } else {
                System.out.println("Invalid username or password.");
            }
        } else {
            System.out.println("No profile found with the provided username.");
        }
    }

    @FXML
    private TextField jhnameField, jhusernameField, jhemailField , companyField;

    @FXML
    private PasswordField jhpasswordField;

    @FXML
    public void register_job_hunter() {
        String username = jhusernameField.getText();
        String password = jhpasswordField.getText();
        String email = jhemailField.getText();
        String name = jhnameField.getText();

        if (db.isProfile(username)) {
            System.out.println("Profile already exists. Please login instead.");
            return;
        }

        db.addProfile(username, name, email, password, "JobHunter");
        db.add_jobhunter(name, username, password, email);
        curr_user = username;
        curr_type = "JobHunter";
        System.out.println("Registration successful! Welcome, " + curr_user);
        UserSession.currentRole = curr_type ; 
        UserSession.currentUsername = curr_user ;
        // After successful registration, redirect to login or home page
        goToResumeBuilder();
    }
    @FXML
    public void goToResumeBuilder() {
    	  try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("ResumeBuilder.fxml"));
              Scene ResumeScene = new Scene(loader.load());
              Stage stage = (Stage) jhusernameField.getScene().getWindow();
              stage.setScene(ResumeScene);
          } catch (IOException e) {
              e.printStackTrace();
          }
    }
    
    @FXML
    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) jhusernameField.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void goToAccount() {
    	try {
    		System.out.println("Controller.goToAccount()");
    		 FXMLLoader loader = new FXMLLoader(getClass().getResource("account1.fxml"));
             Scene accountScene = new Scene(loader.load());
             Stage stage = (Stage) usernameField.getScene().getWindow();
             stage.setScene(accountScene);
    		
    	} catch (IOException e ) {
    		e.printStackTrace();
    	}
    	
    	
    	
    }
    @FXML
    public void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registerJH.fxml"));
            Scene registerScene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(registerScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void register_employer() {
        String username = jhusernameField.getText().trim();
        String password = jhpasswordField.getText().trim();
        String email = jhemailField.getText().trim();
        String name = jhnameField.getText().trim();
        String company_name = companyField.getText().trim();

        if (db.isProfile(username)) {
            System.out.println("Profile already exists. Please log in.");
            return;
        }

        if (db.isCompany(company_name)) {
            System.out.println("Company does not exist. Please register the company first.");
            return;
        }

        db.addProfile(username, name, email, password, "Employer");
        db.add_employer(name, username, password, email, company_name);

        curr_user = username;
        curr_type = "Employer";  

        System.out.println("Registration successful. Welcome, " + name + "!");
    }
    public void register_recruiter()
    {
        String username =jhusernameField.getText();
        String password=jhusernameField.getText();
        String email=jhusernameField.getText();
        String name=jhusernameField.getText();
        if(db.isProfile(username))
        {
            //profile already exists login?
        	return;
        }
        db.addProfile(username,name,email,password,"Recruiter");
        db.add_recruiter(name, username, password, email);
        curr_user = username;
        curr_type = "Recruiter";
    }

}



