import java.sql.*;
import java.io.*;
import java.util.Scanner;

public class Admin {
	private static Connection conn = null;
	
	private static class Query{
    	public static final String[] CREATETABLE = {
                "CREATE TABLE category (cID INTEGER UNSIGNED, cName CHAR(20) NOT NULL, PRIMARY KEY(cID))",
                "CREATE TABLE manufacturer (mID INTEGER UNSIGNED, mName CHAR(20) NOT NULL, mAddress CHAR(50) NOT NULL, mPhoneNumber INTEGER UNSIGNED NOT NULL, PRIMARY KEY(mID))",
                "CREATE TABLE part (pID INTEGER UNSIGNED, pName CHAR(20) NOT NULL, pPrice INTEGER UNSIGNED NOT NULL, mID INTEGER UNSIGNED NOT NULL, cID INTEGER UNSIGNED NOT NULL, pWarrantyPeriod INTEGER UNSIGNED NOT NULL, pAvailableQuantity INTEGER UNSIGNED NOT NULL, PRIMARY KEY(pID), FOREIGN KEY (mID) REFERENCES manufacturer(mID) ON DELETE CASCADE ON UPDATE NO ACTION, FOREIGN KEY (cID) REFERENCES category(cID) ON DELETE CASCADE ON UPDATE NO ACTION)", 
                "CREATE TABLE salesperson (sID INTEGER UNSIGNED, sName CHAR(20) NOT NULL, sAddress CHAR(50) NOT NULL, sPhoneNumber INTEGER UNSIGNED NOT NULL, sExperience INTEGER UNSIGNED NOT NULL, PRIMARY KEY(sID))",
                "CREATE TABLE transaction(tID INTEGER UNSIGNED, pID INTEGER UNSIGNED NOT NULL, sID INTEGER UNSIGNED NOT NULL, tDate DATE DEFAULT '00/00/0000' NOT NULL, PRIMARY KEY(tID), FOREIGN KEY (pID) REFERENCES part(pID) ON DELETE CASCADE ON UPDATE NO ACTION, FOREIGN KEY (sID) REFERENCES salesperson(sID) ON DELETE CASCADE ON UPDATE NO ACTION)"
             };

        public static final String[] DROPTABLE = {
        		"DROP TABLE IF EXISTS transaction",
                "DROP TABLE IF EXISTS salesperson",
                "DROP TABLE IF EXISTS part",
                "DROP TABLE IF EXISTS manufacturer",
                "DROP TABLE IF EXISTS category"
             };
        
        public static final String[] SHOWTABLEDETAIL = {
                "SELECT COUNT(*) FROM category",
                "SELECT COUNT(*) FROM manufacturer",
                "SELECT COUNT(*) FROM part",
                "SELECT COUNT(*) FROM salesperson",
                "SELECT COUNT(*) FROM transaction"
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
                "INSERT INTO transaction VALUES(?,?,?,?)";
   
    }
	
	public static void connectDB() { //can be replace by the same method in Preprocessing	
		try{
    		conn = DriverManager.getConnection(PreProcessing.DB_Url ,PreProcessing.DB_User, PreProcessing.DB_Password);
    		
    	}catch(SQLException e){
    		System.out.println("\nCant connect to database");
    	}
	}
	
	private static void closeConDB() throws SQLException{ //can be replace by same method in Preprocessing
    	conn.close();
    }
	
	public static boolean createTable() throws SQLException{
		boolean succ = true;
		connectDB();
		Statement stmt = conn.createStatement();
		String [] query = Admin.Query.CREATETABLE;
		for (String sql : query){
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				succ = false;
				break;
			}
		}
		closeConDB();
		return succ;
	}
	
	public static boolean deleteTable() throws SQLException {
		boolean succ = true;
		connectDB();
		Statement stmt = conn.createStatement();
		String [] query = Admin.Query.DROPTABLE;
		for (String sql : query){
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				succ = false;
				break;
			}
		}
		closeConDB();
		return succ;
	}
	
	public static boolean loadData(String path) throws SQLException {
		boolean succ = true;
		String date, tDate;
		connectDB();
		PreparedStatement pstmt = null;
		try {
			File file = new File(path+"category.txt");
            pstmt = conn.prepareStatement(Admin.Query.INSERTCATEGORY);
            Scanner sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
                pstmt.setInt(1, sc.nextInt());//c_id
                pstmt.setString(2, sc.next());//c_name
                pstmt.executeUpdate();
            }
            
            file = new File(path+"manufacturer.txt");
            pstmt = conn.prepareStatement(Admin.Query.INSERTMANUACTURER);
            sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
            	pstmt.setInt(1, sc.nextInt());//m_id
                pstmt.setString(2, sc.next());//m_name
                pstmt.setString(3, sc.next());//m_address
                pstmt.setInt(4, sc.nextInt());//m_phone number
                pstmt.executeUpdate();
            }
            
            file = new File(path+"part.txt");
            pstmt = conn.prepareStatement(Admin.Query.INSERTPART);
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
            
            file = new File(path+"salesperson.txt");
            pstmt = conn.prepareStatement(Admin.Query.INSERTSALESPERSONS);
            sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
            	pstmt.setInt(1, sc.nextInt());//s_id
                pstmt.setString(2, sc.next());//s_name
                pstmt.setString(3, sc.next());//s_address
                pstmt.setInt(4, sc.nextInt());//s_phone
                pstmt.setInt(5, sc.nextInt());//s_phone
                pstmt.executeUpdate();
            }
            
            file = new File(path+"transaction.txt");
            pstmt = conn.prepareStatement(Admin.Query.INSERTTRANSACTIONS);
            sc = new Scanner(file).useDelimiter("\t|\n");
            while (sc.hasNext() == true) {
            	pstmt.setInt(1, sc.nextInt());//t_id
                pstmt.setInt(2, sc.nextInt());//p_id
                pstmt.setInt(3, sc.nextInt());//s_id
                date = sc.next();
                tDate = date.substring(6,9) + '-' + date.substring(3,4) + '-' + date.substring(0,1);
                pstmt.setString(4, tDate);//t_date
                pstmt.executeUpdate();
            }
			
		} catch (Exception e) {
			succ = false;
		}
		
		if (pstmt != null) {
			pstmt.close();
		}	
		closeConDB();
		return succ;
	}
	
	public static String showRecords() throws SQLException {
		String result = "";
		String [] table = {"category: ", "manufacturer:", "part:", "salesperson:", "transaction:"};
    	connectDB();
    	ResultSet rs = null;
    	Statement stmt = conn.createStatement();
    	for (int i = 0; i < table.length; i++) {
    		try {
                rs = stmt.executeQuery(Admin.Query.SHOWTABLEDETAIL[i]);
                rs.next();
                result += table[i] + rs.getInt(1) + "\n";	
			} catch (SQLException e) {
				result = "error";
//				e.printStackTrace();
				break;
			}
		}
    	
    	if (rs != null) {
			rs.close();
		}	
    	
    	closeConDB();
    	return result;
	}
	
	// just for test	
	/*
	public static void main(String[] args) throws SQLException {
		Admin admin = new Admin();
		boolean succ;
		String result;
		succ = admin.createTable();
		System.out.println(succ); 
		succ = admin.loadData("./sample_data/");
		System.out.println(succ); 
		result = admin.showRecords();
		System.out.println(result); 
		succ = admin.deleteTable();
		System.out.println(succ); 
		
	}
*/
}
