import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
		
			String [] query = Administrator.Query.CREATETABLE;
			for (String sql : query){
				stmt.executeUpdate(sql);
			}
			isTableCreated = true;
		
	
		closeConDB();
		
	}
    public static void deleteTable() throws SQLException{
    	
       connectDB();	
			String [] query = Administrator.Query.DROPTABLE;
			for (String sql : query){
				stmt.executeUpdate(sql);
			}
			isTableCreated = false;
		
		closeConDB();
		
	}
    
    public static void loadData(String path) throws SQLException{
    	
    	connectDB();
    	
    	try {



            DatabaseMetaData dbmd = conn.getMetaData();

            String[] types = {"TABLE"};

            ResultSet rs = dbmd.getTables(null, null, "%", types);

            while (rs.next()) {

                System.out.println(rs.getString("TABLE_NAME"));

            }

        } 

            catch (SQLException e) {

            e.printStackTrace();

        }
    	 String current = null;
		try {
			current = new java.io.File( "." ).getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         System.out.println("Current dir:"+current);
    	try{ // try to load file
            File file = new File("category.txt");
            pstmt = conn.prepareStatement(Administrator.Query.INSERTCATEGORY);
            Scanner sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
                pstmt.setInt(1, sc.nextInt());//c_id
                pstmt.setString(2, sc.next());//c_name
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
            
            file = new File("manufacturer.txt");
            pstmt = conn.prepareStatement(Administrator.Query.INSERTMANUACTURER);
            sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
            	pstmt.setInt(1, sc.nextInt());//m_id
                pstmt.setString(2, sc.next());//m_name
                pstmt.setString(3, sc.next());//m_address
                pstmt.setInt(4, sc.nextInt());//m_phone number
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
            
            file = new File("part.txt");
            pstmt = conn.prepareStatement(Administrator.Query.INSERTPART);
            sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
            	pstmt.setInt(1, sc.nextInt());//p_id
                pstmt.setString(2, sc.next());//p_name
                pstmt.setInt(3, sc.nextInt());//p_price
                pstmt.setInt(4, sc.nextInt());//m_id
                pstmt.setInt(5, sc.nextInt());//c_id
                pstmt.setInt(6, sc.nextInt());//p_quality
                pstmt.setInt(7, sc.nextInt());//p_quality
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
            
            file = new File("salesperson.txt");
            pstmt = conn.prepareStatement(Administrator.Query.INSERTSALESPERSONS);
            sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
            	pstmt.setInt(1, sc.nextInt());//s_id
                pstmt.setString(2, sc.next());//s_name
                pstmt.setString(3, sc.next());//s_address
                pstmt.setInt(4, sc.nextInt());//s_phone
                pstmt.setInt(5, sc.nextInt());//s_phone
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
            
            file = new File("transaction.txt");
            pstmt = conn.prepareStatement(Administrator.Query.INSERTTRANSACTIONS);
            sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
            	pstmt.setInt(1, sc.nextInt());//t_id
                pstmt.setInt(2, sc.nextInt());//p_id
                pstmt.setInt(3, sc.nextInt());//s_id
                pstmt.setString(4, sc.next());//t_date
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
            

        } catch (FileNotFoundException e) {
        	e.printStackTrace();
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
    	if(rs == null){
    		
    	} else {
    		rs.close();
    	}
    	
    	stmt.close();
    	conn.close();
    }
    
    private static class Query{
    	public static final String[] CREATETABLE = {

                "CREATE TABLE category (cID Integer PRIMARY KEY,cName varchar(255))",
                "CREATE TABLE manufacturer (mID Integer PRIMARY KEY,mName varchar(255),mAddress varchar(255),mPhoneNumber Integer)",
                "CREATE TABLE part (pID Integer PRIMARY KEY,pName varchar(255),pPrice Integer, mID Integer references manufacturer(mID),cID Integer references category(cID),pWarrentyPeriod Integer, pAvailableQuantity Integer)", 
                "CREATE TABLE salesperson (sID Integer PRIMARY KEY,sName varchar(255),sAddress varchar(255),sPhoneNumber Integer, sExperience Integer)",
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
        
        public final static String INSERTCATEGORY =
                "INSERT INTO category VALUES(?,?)";

         public final static String INSERTMANUACTURER =
                "INSERT INTO manufacturer VALUES(?,?,?,?)";

         public final static String INSERTPART =
                "INSERT INTO part VALUES(?,?,?,?,?,?,?)";

         public final static String INSERTSALESPERSONS =
                "INSERT INTO salesperson VALUES(?,?,?,?,?)";

         public final static String INSERTTRANSACTIONS =
                "INSERT INTO transactions VALUES(?,?,?,tDate(?, 'dd/mm/yyyy'))";
   
    }
    

}
