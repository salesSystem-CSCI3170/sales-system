import java.io.IOException;
import java.sql.*;
import java.lang.*;

public class Salesperson {
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	static String result = null;
	
    public static String SearchStock(String keyword, int type, int order) throws SQLException{
		Salesperson.connectDB();
		// by part name
		if (type == 1){
			if (order == 1){
				pstmt = conn.prepareStatement(Salesperson.Query.SEARCHPARTS[0]);
			} else {
				pstmt = conn.prepareStatement(Salesperson.Query.SEARCHPARTS[2]);
			}
			pstmt.setString(1, "%"+keyword+"%");
			rs = pstmt.executeQuery(); // get query result
			
			
		} else if (type == 2){  // by manufacturer name
			if (order == 1){
				pstmt = conn.prepareStatement(Salesperson.Query.SEARCHPARTS[1]);
			} else {
				pstmt = conn.prepareStatement(Salesperson.Query.SEARCHPARTS[3]);
			}
			pstmt.setString(1, "%"+keyword+"%");
			rs = pstmt.executeQuery(); // get query result
		} else {
			System.out.println("Wrong type!");
			System.out.println("Back to Main Menu!");
            PreProcessing.main_menu();
            
		}
		if (rs.isBeforeFirst()){
			result = "| Part ID | Part Name | Manufacturer Name | Category Name | Available Quantity | Warranty Period | Part Price | \n";
			while (rs.next()){
				result += "|   "+rs.getInt(1) + "   |   " + rs.getString(2) + "   |   "
						    + rs.getString(3) + "   |   " + rs.getString(4) + "   |   " 
						    + rs.getInt(5) + "   |   " + rs.getInt(6) + "   |   " + rs.getInt(7) + "   | \n";
				
			}
		} else {
			result = "Record not found!\nBack to salesperson menu";
		}
		
		closeConDB();
		return result;
		
		
	}
    public static String MakeTransaction(int partid, int saleid) throws SQLException{
		connectDB();
		String result = null;
		// check with the stock availability
		int nextTransaction = 0;
		stmt = conn.createStatement();
		rs = stmt.executeQuery(Salesperson.Query.GETTRANSACTIONID);
		if (rs.next()) nextTransaction = rs.getInt(1)  + 1;
        
		
		if (checkStock(partid, saleid)){
			//add transaction
			pstmt = conn.prepareStatement(Salesperson.Query.ADDTRANSACTION[0]);
			pstmt.setInt(1,nextTransaction);
			pstmt.setInt(2, partid);
            pstmt.setInt(3, saleid);
            pstmt.executeUpdate();
            
            //update the quantity
            pstmt = conn.prepareStatement(Salesperson.Query.ADDTRANSACTION[1]);
            pstmt.setInt(1, partid);
            pstmt.executeUpdate();
            
            //print 
            pstmt = conn.prepareStatement(Salesperson.Query.ADDTRANSACTION[2]);
            pstmt.setInt(1, partid);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	result = "Product:  " + rs.getString(1) + " (id: " + partid +") Remaining Quantity: " + rs.getInt(2);
            } else {
            	result = "Unable to display the available quantity.";
            }
		} else {
			PreProcessing.salesperson();
		}	
		closeConDB();
		return result;
	} 
    private static boolean checkStock (int partid, int saleid) throws SQLException{
    	boolean isAvailable = false;
    	
    	Salesperson.connectDB();
    	// check salesperson
    	pstmt = conn.prepareStatement(Salesperson.Query.CHECK[0]);
    	pstmt.setInt(1, saleid);
    	rs = pstmt.executeQuery();
    	
    	while(rs.next()){
    		if (rs.getInt(1) == 0){
    			System.out.println("Warning: salesperson ID is not exist!");
    			return false;
    		}
    	}
    	// check stock
    	pstmt = conn.prepareStatement(Salesperson.Query.CHECK[2]);
    	pstmt.setInt(1, partid);
    	rs = pstmt.executeQuery();
    	
    	while(rs.next()){
    		if (rs.getInt(1) == 0){
    			System.out.println("Warning: part ID is not exist!");
    			return false;
    		} else {
    			if (rs.getInt(2) == 0){
    				isAvailable = false;
    				break;
    			} else {
    				isAvailable = true;
    				break;
    			}
    		}
    	}
		return isAvailable;
    }
    
    private static void connectDB(){
    	try{
    		conn = DriverManager.getConnection(PreProcessing.DB_Url ,PreProcessing.DB_User, PreProcessing.DB_Password);
    		
    	}catch(SQLException e){
    		System.out.println("\nCant connect to database");
    	}
    }
    private static void closeConDB() throws SQLException{
    	rs.close();
    	pstmt.close();
    	conn.close();
    }
    
    private static class Query {
    	public final static String[] SEARCHPARTS = {
    			"SELECT P.pID, P.pName, M.mName, C.cName, P.pAvailableQuantity, P.pWarrentyPeriod, P.pPrice FROM category C, manufacturer M, part P WHERE P.mID = M.mID AND P.cID = C.cID AND P.pName LIKE ? ORDER BY P.pPrice",
    			"SELECT P.pID, P.pName, M.mName, C.cName, P.pAvailableQuantity, P.pWarrentyPeriod, P.pPrice FROM category C, manufacturer M, part P WHERE P.mID = M.mID AND P.cID = C.cID AND M.mName LIKE ? ORDER BY P.pPrice",
    			"SELECT P.pID, P.pName, M.mName, C.cName, P.pAvailableQuantity, P.pWarrentyPeriod, P.pPrice FROM category C, manufacturer M, part P WHERE P.mID = M.mID AND P.cID = C.cID AND P.pName LIKE ? ORDER BY P.pPrice DESC",
    			"SELECT P.pID, P.pName, M.mName, C.cName, P.pAvailableQuantity, P.pWarrentyPeriod, P.pPrice FROM category C, manufacturer M, part P WHERE P.mID = M.mID AND P.cID = C.cID AND M.mName LIKE ? ORDER BY P.pPrice DESC"

    	};
    	
    	public final static String[] ADDTRANSACTION ={

                "INSERT INTO transactions VALUES (?,?,?, CURRENT_DATE)",
                "UPDATE part SET pAvailableQuantity = pAvailableQuantity - 1 WHERE pID = ?",
                "SELECT P.pName, P.pAvailableQuantity FROM part P WHERE P.pID = ?"

        };
    	
    	public final static String[] CHECK ={

                "SELECT COUNT(*) FROM salesperson S WHERE S.sID = ?",
                "SELECT COUNT(*) FROM part P where P.pID = ?",
                "SELECT pID ,pAvailableQuantity FROM part where pID = ?"
        };
    	
    	public final static String GETTRANSACTIONID = "SELECT COUNT(*) FROM transactions";
    }

}

