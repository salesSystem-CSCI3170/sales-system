import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Administrator {
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	static String result = null;
	
	static boolean isTableCreated = false;
	
	public static void createTable() throws SQLException{
		connectDB();
		
		if (isTableCreated){
			System.out.println("Table already created!");
		} else {
			
			String [] query = Administrator.Query.CREATETABLE;
			for (String sql : query){
				stmt.executeUpdate(sql);
			}
			isTableCreated = true;
		}
		
		closeConDB();
		
	}
    public static void deleteTable() throws SQLException{
    	
       connectDB();
		
		if (!isTableCreated){
			System.out.println("Noting to delete!");
		} else {
			
			String [] query = Administrator.Query.DROPTABLE;
			for (String sql : query){
				stmt.executeUpdate(sql);
			}
			isTableCreated = false;
		}
		
		closeConDB();
		
	}
    
    public static void loadData(String path) throws SQLException{
    	
    	connectDB();
    	try{ // try to load file
            File file = new File(path);
            Scanner sc = new Scanner(file).useDelimiter("\t|\n");

        } catch (FileNotFoundException e) {
            System.err.println(path + " not found.");
            System.out.println("Back to Administrator Menu!");
            PreProcessing.administrator();
        }
    	
    	
    	closeConDB();
		
	}
    public static String showNoOfRecords() throws SQLException{
    	String result = null;
    	connectDB();
    	pstmt = conn.prepareStatement(Administrator.Query.SHOWTABLEDETAIL[0]);
        rs = pstmt.executeQuery();
        result = "Category: " + rs.getInt(1) + "\n";
        pstmt = conn.prepareStatement(Administrator.Query.SHOWTABLEDETAIL[1]);
        rs = pstmt.executeQuery();
        result += "Manufacturer"+ rs.getInt(1) + "\n";
        pstmt = conn.prepareStatement(Administrator.Query.SHOWTABLEDETAIL[2]);
        rs = pstmt.executeQuery();
        result += "Parts"+ rs.getInt(1) + "\n";
        pstmt = conn.prepareStatement(Administrator.Query.SHOWTABLEDETAIL[3]);
        rs = pstmt.executeQuery();
        result += "Salesperson"+ rs.getInt(1) + "\n";
        pstmt = conn.prepareStatement(Administrator.Query.SHOWTABLEDETAIL[4]);
        rs = pstmt.executeQuery();
        result += "Transaction"+ rs.getInt(1) + "\n";
    
    	closeConDB();
    	return result;
	}
    
    
    private static void connectDB() throws SQLException{
    	try{
    		conn = DriverManager.getConnection(PreProcessing.DB_Url ,PreProcessing.DB_User, PreProcessing.DB_Password);
    		stmt = conn.createStatement();
    		
    	}catch(SQLException e){
    		System.out.println("\nCant connect to database");
    		System.out.println("Back to Main Menu!");
    		PreProcessing.main_menu();
    	}
    }
    private static void closeConDB() throws SQLException{
    	rs.close();
    	stmt.close();
    	conn.close();
    }
    
    private static class Query{
    	public static final String[] CREATETABLE = {

                "CREATE TABLE category (cID Integer PRIMARY KEY,cName varchar2(255))",
                "CREATE TABLE manufacturer (mID Integer PRIMARY KEY,mName varchar2(255),mAddress varchar2(255),mPhoneNumber Integer)",
                "CREATE TABLE part (pID Integer PRIMARY KEY,pName varchar2(255),pPrice Integer, mID Integer references manufacturer(mID),cID Integer references category(cID),pWarrentyPeriod Integer, pAvailableQuantity Integer)", 
                "CREATE TABLE salesperson (sID Integer PRIMARY KEY,sName varchar2(255),sAddress varchar2(255),sPhoneNumber Integer, sExperience Integer)",
                "CREATE TABLE transactions (tID Integer PRIMARY KEY,pID Integer references part(pID),sID Integer references salesperson(sID),tDate Date)"
             };

        public static final String[] DROPTABLE = {
                "DROP TABLE transactions",
                "DROP TABLE salesperson",
                "DROP TABLE part",
                "DROP TABLE manufacturer",
                "DROP TABLE category"
             };
        
        public static final String[] SHOWTABLEDETAIL = {
                "SELECT COUNT(*) FROM category",
                "SELECT COUNT(*) FROM manufacturer",
                "SELECT COUNT(*) FROM part",
                "SELECT CONUT(*) FROM salesperson",
                "SELECT COUNT(*) FROM transactions"
             };
   
    }
    

}
