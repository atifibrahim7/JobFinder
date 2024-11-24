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

class DBHandler {
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