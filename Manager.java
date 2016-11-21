/**
 * 
 */
/**
 * @author LENO
 *
 */
//package Database;
import java.sql.*;
import java.io.*;
import java.util.Scanner;
public class Manager
{
	static String receive;
	static String sent;
	//upper and lower bound year for experience
	static int upperbound;
	static int lowerbound;
	//How much part are showed
	static int Numofpart;
	static Connection conn;
	String categoryfile;
	String manufacturerfile;
	String partfile;
	String salespersonfile;
	String transactionfile;
	Manager()
	{
		receive = "";
		sent = "";
		upperbound = 0;
		lowerbound = 0;
		Numofpart = 0;
		categoryfile = "category.txt";
		manufacturerfile = "manufacturer.txt";
		partfile = "part.txt";
		salespersonfile = "salesperson.txt";
		transactionfile = "transaction.txt";
	}
	
	public static void connectDB() 
	{ //can be replace by the same method in Preprocessing
		try{
    		conn = DriverManager.getConnection(PreProcessing.DB_Url ,PreProcessing.DB_User, PreProcessing.DB_Password);
    		
    	}catch(SQLException e){
    		System.out.println("\nCant connect to database");
    	}
		
	}
	
	private static void closeConDB() throws SQLException
	{ //can be replace by same method in Preprocessing
    	conn.close();
    }
	static String countNoOfRecord(int lower, int upper)
	{
		upperbound = upper;
		lowerbound = lower;
		//query sentences
		try 
		{
			//upperbound and lowerbound are variables to be set in the query
			connectDB();
			PreparedStatement pstmt = conn.prepareStatement(
					"SELECT salesperson.sID, sName, COUNT(DISTINCT tID) AS cTRAN "
					+ "FROM salesperson, transaction "
					+ "WHERE transaction.sID = salesperson.sID AND salesperson.sExperience >= ? AND salesperson.sExperience <= ? "
					+ "GROUP BY salesperson.sID "
					+ "ORDER BY salesperson.sID DESC");
			pstmt.setInt(1, lowerbound);
			pstmt.setInt(2, upperbound);
			ResultSet rs = pstmt.executeQuery();
			sent = "| ID | Name | Years of Experience | Number of Transaction |\n";
			while (rs.next()) 
			{
				int sID = rs.getInt("salesperson.sID");
				String sName = rs.getString("sName");
				String cTRAN = rs.getString("cTRAN");
				sent = sent + "| " + sID + " | " + sName + " | " + cTRAN + " |\n";
			}
			pstmt.close();
			closeConDB();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sent;
	}
	static String sortAnListM()
	{
		String query = "SELECT manufacturer.mID, mName, SUM(pPrice) as tPRICE "
				+ "FROM manufacturer, part, transaction "
				+ "WHERE transaction.pID = part.pID AND manufacturer.mID = part.mID "
				+ "GROUP BY manufacturer.mID "
				+ "ORDER BY tPRICE DESC";
		try 
		{
			connectDB();
			Statement statement = (Statement) conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			sent = "| Manufacturer ID | Manufacturer Name | Total Sales Value |\n";
			while (rs.next()) 
			{
				int mID = rs.getInt("manufacturer.mID");
				String mName = rs.getString("mName");
				String tPRICE = rs.getString("tPRICE");
				sent = sent + "| " + mID + " | " + mName + " | " + tPRICE + " |\n";
			}
			statement.close();
			closeConDB();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sent;
	}
	public static String showNamesPopularPart(int n)
	{
		Numofpart = n;
		int count = 0;
		String query = "SELECT part.pID, pName, COUNT(DISTINCT tID) as noTRAN "
				+ "FROM part, transaction "
				+ "WHERE transaction.pID = part.pID "
				+ "GROUP BY part.pID "
				+ "ORDER BY noTRAN DESC";
		try 
		{
			connectDB();
			Statement statement = (Statement) conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			sent = "| Part ID | Part Name | No. of Transaction |\n";
			while (rs.next()) 
			{
				int pID = rs.getInt("pID");
				String pName = rs.getString("pName");
				String noTRAN = rs.getString("noTRAN");
				sent = sent + "| " + pID + " | " +  pName + " | " + noTRAN + " |\n";
				count++;
				if(count >= n)
				{
					break;
				}
			}
			statement.close();
			closeConDB();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sent;
	}
}