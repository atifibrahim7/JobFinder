package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
class DBHandler {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345678";

    private Connection conn;
    
    private static DBHandler instance;

    // Constructor to establish the connection
    private DBHandler() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to the database.");
            System.exit(1); // Terminate the program on connection failure
        }
    }
    

    public static DBHandler getInstance() {
        if (instance == null) {
            instance = new DBHandler();
        }
        return instance;
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
    
    
    public void addEndorsement(String jobHunterUsername, String endorserUsername, String description) {
        String query = "INSERT INTO Endorsements (job_hunter_username, endorser_username, description, date_endorsed) " +
                      "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Get current date
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
           
            pstmt.setString(1, jobHunterUsername);
            pstmt.setString(2, endorserUsername);
            pstmt.setString(3, description);
            pstmt.setDate(4, currentDate);
           
            pstmt.executeUpdate();
            System.out.println("Endorsement added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add endorsement");
            e.printStackTrace();
            throw new RuntimeException("Failed to add endorsement", e);
        }
    }
   
    public ArrayList<Endorsement> getEndorsementsByEndorser(String endorserUsername) {
        ArrayList<Endorsement> endorsements = new ArrayList<>();
        String query = "SELECT * FROM Endorsements WHERE endorser_username = ?";
       
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, endorserUsername);
            ResultSet rs = pstmt.executeQuery();
           
            while (rs.next()) {
                Endorsement endorsement = new Endorsement(
                    rs.getString("job_hunter_username"),
                    rs.getString("endorser_username"),
                    rs.getString("description"),
                    rs.getDate("date_endorsed").toString()
                );
                endorsements.add(endorsement);
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to fetch endorsements");
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch endorsements", e);
        }
       
        return endorsements;
    }

    
    public void update_application(String new_status,String vacancy_title, String username)
    {
    	String query = "UPDATE application SET status = ? WHERE vacancy_title = ? AND applicant_username = ?";
    	try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, new_status);
            pstmt.setString(2, vacancy_title);
            pstmt.setString(3, username);
            pstmt.executeUpdate();
            System.out.println("Application Status Updated");
        } catch (SQLException e) {
            System.err.println("failed to update application");
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
    
    public ArrayList<ArrayList<String>> getJHApplications(String JHusername)
    {
    	String query = "select company,requirements,jobvacancy.vacancy_title,status from jobvacancy join application on jobvacancy.vacancy_title = application.vacancy_title where application.applicant_username = ?";
        ArrayList<ArrayList<String>> applications = new ArrayList<>();
        try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, JHusername);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	ArrayList<String> application1 = new ArrayList<>();
	 	        application1.add(rs.getString("company"));
	 	        application1.add(rs.getString("requirements"));
	 	        application1.add(rs.getString("vacancy_title"));
	 	        application1.add(rs.getString("status"));
	 	        applications.add(application1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving companies: " + e.getMessage());
        }
        return applications;

    }
    
    public ArrayList<ArrayList<String>> getApplications(String username)
    {
    	String query = "select company,requirements,jobvacancy.vacancy_title,applicant_username,status from jobvacancy join application on jobvacancy.vacancy_title = application.vacancy_title "
    			+ "where jobvacancy.recruiter_username = ?";
        ArrayList<ArrayList<String>> applications = new ArrayList<>();
        try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	String temp = rs.getString("status");
            	if("pending".equals(temp)) {
 	                ArrayList<String> application1 = new ArrayList<>();
 	                application1.add(rs.getString("company"));
 	                application1.add(rs.getString("requirements"));
 	                application1.add(rs.getString("vacancy_title"));
 	                application1.add(rs.getString("applicant_username"));
 	                applications.add(application1);
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving companies: " + e.getMessage());
        }
        return applications;
    }

    public void addApplication(String username, String vacancyTitle, Date applicationDate) {
        String query = "INSERT INTO application (applicant_username,date_applied ,  vacancy_title , status ) " +
                      "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setDate(2, applicationDate);
            pstmt.setString(3, vacancyTitle);
            pstmt.setString(4, "pending");
            pstmt.executeUpdate();
            System.out.println("Application submitted successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to submit application");
            e.printStackTrace();
            throw new RuntimeException("Failed to submit application", e);
        }
    }

    public JobVacancyDetails getJobVacancyDetails(String vacancyTitle) {
        String query = "SELECT * FROM JobVacancy WHERE vacancy_title = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, vacancyTitle);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new JobVacancyDetails(
                    rs.getString("vacancy_title"),
                    rs.getString("company"),
                    rs.getString("location"),
                    rs.getDate("date_posted"),
                    rs.getDate("deadline"),
                    rs.getString("details"),
                    rs.getString("requirements")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve job vacancy details");
            e.printStackTrace();
        }
        return null;
    }

    public static class JobVacancyDetails {
        public final String title;
        public final String company;
        public final String location;
        public final Date datePosted;
        public final Date deadline;
        public final String details;
        public final String requirements;
        
        public JobVacancyDetails(String title, String company, String location, 
                               Date datePosted, Date deadline, String details, 
                               String requirements) {
            this.title = title;
            this.company = company;
            this.location = location;
            this.datePosted = datePosted;
            this.deadline = deadline;
            this.details = details;
            this.requirements = requirements;
        }
    }
    // Add a Profile
    public void addProfile(String username, String name, String email, String password, String type) {
    	String query = "INSERT INTO profile (username, name, email, password, type) VALUES (?, ?, ?, ?, ?::user_type)";

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
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public ArrayList<ArrayList<String>> getAllCompanies() {
        ArrayList<ArrayList<String>> companies = new ArrayList<>();
        
        String query = "SELECT * FROM company";        
          try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ArrayList<String> companyInfo = new ArrayList<>();
                companyInfo.add(rs.getString("name"));
                companyInfo.add(rs.getString("address"));
                companyInfo.add(rs.getString("email"));
                
                companies.add(companyInfo);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving companies: " + e.getMessage());
        }
        
        return companies;
    }

public List<String> getJobVacanciesByCompany(String companyName) {
    List<String> jobVacancies = new ArrayList<>();
    String query = "SELECT vacancy_title FROM JobVacancy WHERE company = (SELECT name FROM Company WHERE name = ?)";

    try ( PreparedStatement pstmt = conn.prepareStatement(query)) {

    	pstmt.setString(1, companyName);

        try (ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                jobVacancies.add(resultSet.getString("vacancy_title"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return jobVacancies;
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
            e.printStackTrace(); // Print the stack trace for debugging
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
    
    public boolean isResume(String username)
    {
    	String query = "SELECT * FROM Resume WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if username exists, false if it doesn't
        } catch (SQLException e) {
            System.err.println("Error: Unable to check resume for username: " + username);
            return false; // Return false in case of database errors
        }
    }
    public boolean isRecruiter(String username) {
        String query = "SELECT * FROM recruiter WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if recruiter exists, false if it doesn't
        } catch (SQLException e) {
            System.err.println("Error: Unable to check profile for username: " + username);
            return false; // Return false in case of database errors
        }
    }
    public boolean isCompany(String name) {
        String query = "SELECT * FROM Company WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if company exists, false if it doesn't
        } catch (SQLException e) {
            System.err.println("Error: Unable to check company" + name);
            return false; // Return false in case of database errors
        }
    }

    public Map<String, String> verify(String username, String password) {
        String query = "SELECT * FROM Profile WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Extract user details into a map
                    Map<String, String> userDetails = new HashMap<>();
                    userDetails.put("username", rs.getString("username"));
                    userDetails.put("type", rs.getString("type"));
                    userDetails.put("email",rs.getString("email"));
                    userDetails.put("name",rs.getString("name"));
                    return userDetails;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to verify credentials for username: " + username);
            e.printStackTrace();
        }
        return null; // Return null if no user is found or an error occurs
    }
    public void addResume(String username,String education,String experience,String skills)
    {
    	String query = "Insert Into resume (username,education,past_experience,skills) values (?,?,?,?)";
    	 try (PreparedStatement pstmt = conn.prepareStatement(query)) {
             pstmt.setString(1, username);
             pstmt.setString(2, education);
             pstmt.setString(3, experience);
             pstmt.setString(4, skills);
             pstmt.executeUpdate();
             System.out.println("Resume added successfully.");
         } catch (SQLException e) {
             System.err.println("Error: Unable to add Resume for username: " + username);
         }
    	
    }
    
    public void addVacancy(String company, String details, String requirements, String location, 
            String datePosted, String deadline, String recruiter) {
        String query = "INSERT INTO JobVacancy (company, details, requirements, location, date_posted, deadline, recruiter_username, vacancy_title) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Generate a unique vacancy title using timestamp
            String vacancyTitle = company + "_" + System.currentTimeMillis();

            // Convert String dates to java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date sqlDatePosted = new java.sql.Date(sdf.parse(datePosted).getTime());
            java.sql.Date sqlDeadline = new java.sql.Date(sdf.parse(deadline).getTime());

            pstmt.setString(1, company);
            pstmt.setString(2, details);
            pstmt.setString(3, requirements);
            pstmt.setString(4, location);
            pstmt.setDate(5, sqlDatePosted);  // Use setDate for DATE type
            pstmt.setDate(6, sqlDeadline);    // Use setDate for DATE type
            pstmt.setString(7, recruiter);
            pstmt.setString(8, vacancyTitle);

            pstmt.executeUpdate();
            System.out.println("Vacancy added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add Vacancy");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
            e.printStackTrace();
        }
    }
    
    public ArrayList<String> getResume(String username)
    {
    	String query = "SELECT * FROM resume WHERE username = ?";
    	ArrayList<String> userInfo = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            System.out.println("Executing query: " + query);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int columnCount = rs.getMetaData().getColumnCount(); // Get number of columns
                    for (int i = 1; i <= columnCount; i++) { // Iterate over each column
                        userInfo.add(rs.getString(i)); // Add column value as a string
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to get User: " + username);
            e.printStackTrace();
            return null;
        }
        return userInfo;
    }
    
    public ArrayList<String> getUser(String username, String type) {
        // Validate the table name to avoid SQL injection
        String query = "SELECT * FROM " + type + " WHERE username = ?";
        ArrayList<String> userInfo = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            System.out.println("Executing query: " + query);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int columnCount = rs.getMetaData().getColumnCount(); // Get number of columns
                    for (int i = 1; i <= columnCount; i++) { // Iterate over each column
                        userInfo.add(rs.getString(i)); // Add column value as a string
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to get User: " + username);
            e.printStackTrace();
            return null;
        }
        
        return userInfo;
    }

    
    public void add_jobhunter(String name,String username,String password,String email)
    {
    	String query = "INSERT INTO JobHunter (username, name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            System.out.println("JH added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add JobHunter for username: " + username);
        }
    }
    public void add_recruiter(String name, String username, String password, String email) {
        String query = "INSERT INTO Recruiter (username, name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            System.out.println("Recruiter added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add recruiter for username: " + username);
        }
    }
    public void add_employer(String name, String username, String password, String email, String company) {
        String query = "INSERT INTO Employer (username, name, email, password, company) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, company); 
            pstmt.executeUpdate();
            System.out.println("Employer added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add employer for username: " + username);
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