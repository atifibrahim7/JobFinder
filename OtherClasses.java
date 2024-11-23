package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OtherClasses {
	public class DBHandler {
	    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
	    private static final String DB_USER = "postgres";
	    private static final String DB_PASSWORD = "abbasi123";

	    private Connection conn;

	    // Constructor to establish the connection
	    public DBHandler() {
	        try {
	            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	            System.out.println("Database connected successfully.");
	        } catch (SQLException e) {
	            System.err.println("Error: Failed to connect to the database.");
	            System.exit(1); // Terminate the program on connection failure
	        }
	    }

	    public void test()
	    {
	        // Database credentials and URL

	        // Test the connection
	        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	            System.out.println("Connection successful!");

	            // Create and execute a query
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT 1"); // Simple test query

	            // Process the result
	            while (resultSet.next()) {
	                System.out.println("Query Result: " + resultSet.getInt(1));
	            }
	        } catch (Exception e) {
	            System.out.println("Connection failed: " + e.getMessage());

	        }
	    }
	    // Add a Profile
	    public void addProfile(String username, String name, String email, String password,String type) {
	        String query = "INSERT INTO Profile (username, name, email, password) VALUES (?, ?, ?, ?, ?)";
	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, name);
	            pstmt.setString(3, email);
	            pstmt.setString(4, password);
	            pstmt.setString(5, type);
	            pstmt.executeUpdate();
	            System.out.println("Profile added successfully.");
	        } catch (SQLException e) {
	            System.err.println("Error: Unable to add profile for username: " + username);
	        }
	    }

	    // Retrieve all JobHunters
	    public List<String> getAllJobHunters() {
	        List<String> jobHunters = new ArrayList<>();
	        String query = "SELECT * FROM JobHunter";
	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                String username = rs.getString("username");
	                String name = rs.getString("name");
	                jobHunters.add("Username: " + username + ", Name: " + name);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error: Unable to retrieve job hunters.");
	        }
	        return jobHunters;
	    }

	    public boolean isProfile(String username) {
	        String query = "SELECT * FROM Profile WHERE username = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, username);
	            ResultSet rs = pstmt.executeQuery();
	            return rs.next(); // Returns true if username exists, false if it doesn't
	        } catch (SQLException e) {
	            System.err.println("Error: Unable to check profile for username: " + username);
	            return false; // Return false in case of database errors
	        }
	    }

	    public boolean verify(String username, String password) {
	        String query = "SELECT * FROM Profile WHERE username = ? AND password = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, password);
	            ResultSet rs = pstmt.executeQuery();
	            return rs.next(); // Returns true if username and password match, false if they don't
	        } catch (SQLException e) {
	            System.err.println("Error: Unable to verify credentials for username: " + username);
	            return false; // Return false in case of database errors
	        }
	    }

	    public ResultSet getUser(String username,String type) {
	        String query = "SELECT * FROM ? WHERE username = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, type);
	            return pstmt.executeQuery();
	        } catch (SQLException e) {
	            System.err.println("Error: Unable to verify credentials for username: " + username);
	            return null; // Return false in case of database errors
	        }
	    }
	    // Delete a Profile
	    public void deleteProfile(String username) {
	        String query = "DELETE FROM Profile WHERE username = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, username);
	            pstmt.executeUpdate();
	            System.out.println("Profile deleted successfully.");
	        } catch (SQLException e) {
	            System.err.println("Error: Unable to delete profile for username: " + username);
	        }
	    }

	    // Close the connection
	    public void close() {
	        try {
	            if (conn != null) {
	                conn.close();
	                System.out.println("Connection closed.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error: Unable to close the database connection.");
	        }
	    }

	}

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



}
