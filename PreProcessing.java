import java.io.IOException;
import java.util.Scanner;
import java.sql.*;



public class PreProcessing {
	
	//message show
	private static final String WELCOME_MESSAGE = "Welcome to sales system!";
	private static final String MAIN_MENU = "-----Main menu-----";
	private static final String ASK_FOR_OPERATION = "What kinds of operation would you like to perform?";
	private static final String MAIN_MENU_LIST = "1. Operations for administraator \n"
			+ "2. Operations for salesperson \n"
			+ "3. Operations for manager \n"
			+ "4. Exit this program";
	private static final String ENTER_CHOICE = "Enter Your Choice: ";
	private static final String ADMINISTRATOR_MENU = "-----Operations for administrator menu-----";
	private static final String ADMINISTRATOR_MENU_LIST = "1. Create all tables \n"
			+ "2. Delete all tables \n"
			+ "3. Load from datafile \n"
			+ "4. Show number of records in each table \n"
			+ "5. Return to the main menu";
	private static final String CREATE_TABLE_COMPLETE = "Processing...Done! Database is initialized!";
	private static final String DELETE_TABLE_COMPLETE = "Processing...Done! Database is removed!";
	private static final String ASK_FOR_PATH = "Type in the Source Data Folder Path: ";
	private static final String LOAD_DATA_COMPLETE = "Processing...Done! Data is inputted to the database!";
	private static final String SHOW_RECORD = "Number of records in each table: ";
	
	private static final String SALESPERSON_MENU = "-----Operations for salesperson menu-----";
	private static final String SALESPERSON_LIST = "1. Search for parts \n"
			+ "2. Sell a part \n"
			+ "3. Return to the main menu";
	private static final String CHOOSE_SEARCH_CRITERION = "Choose the Search criterion: ";
	private static final String SEARCH_CRITERION = "1. Part Name \n"
			+ "2. Manufacturer Name";
	private static final String ASK_FOR_KEYWORD = "Type in the Search Keyword: ";
	private static final String CHOOSE_ORDER = "Choose ordering: ";
	private static final String ORDER_LIST = "1. By price, asceding order \n"
			+ "2. By price, descending order";
	private static final String END_OF_QUERY = "End of Query";
	private static final String ASK_FOR_PARTID = "Enter the Part ID: ";
	private static final String ASK_FOR_SALPID = "Enter the Salesperson ID: ";
	private static final String SHOW_REMAINING = "Product: %s (id: %d) Remaining Quantity: %d";
	
	private static final String MANAGER_MENU = "-----Operations for manager menu-----";
	private static final String MANAGER_LIST = "1. Count the no. of sales record of each salesperson under a specific range on years \n"
			+ "2. Show the total sales value of each manufacturer \n"
			+ "3. Show the N most popular part \n"
			+ "4. Return to the main menu\n";
	private static final String LOWER_EXPERIENCE = "Type in the lower bound for years of experience: ";
	private static final String UPPER_EXPERIENCE = "Type in the upper bound for years of experience: ";
	private static final String PRINT_RECORD = "Transaction Record: ";
	private static final String TYPE_NO_OF_PARTS = "Type in the number of parts: ";
	
	
	
	
	static Connection conn = null;
	public final static String DB_Url = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2712/db135?autoReconnect=true&useSSL=false";
    public final static String DB_User = "db135";
    public final static String DB_Password ="f876dccf";
    
    
    //error message
    private final static String INVALID_OPERATION = "Error! Please select again.";
    private final static String INVALID_ORDER = "Error! Please enter a valid order number";
    private final static String INVALID_TYPE = "Error! Please enter a valid type";
    
    
    static String returnMessage = null;
    
    
    
	public static void main(String[] args) throws IOException, SQLException
	{
		//Load JDBC Driver
		try {
		
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			System.err.println("Unable to load the driver class.");
			e.printStackTrace();
			return;
		}
		System.out.println("load jdbc driver successful");
		
		try {
	        conn = DriverManager.getConnection(DB_Url, DB_User, DB_Password);
	     }
	     catch (Exception e ) {
	    	 e.printStackTrace();
	     }
		//conn.close();

	    System.out.println(WELCOME_MESSAGE);
	    main_menu();
	    	
	}
	
	public static void main_menu() throws SQLException {
		Scanner input = new Scanner(System.in);
    	while (true)
    	{
    		System.out.println(MAIN_MENU);
    		System.out.println(ASK_FOR_OPERATION);
    		System.out.println(MAIN_MENU_LIST);
    		System.out.print(ENTER_CHOICE);
    		if (input.hasNextInt()){
    			
    			switch (input.nextInt()){
    			case 1: 
    				administrator();
    				break;
    			case 2: 
    				salesperson();
    				break;
    			case 3:
    				manager();
    				break;
    			case 4:
    				System.exit(0);
    			default:
    				System.out.println(INVALID_OPERATION);
    			
    			}
    		} else {
    			System.out.println(INVALID_OPERATION);
    			input.next();
    		}
    	}	
	}
	
	public static void administrator() throws SQLException{
		Scanner sc = new Scanner(System.in);
		String result = null;
		while (true){
			
			System.out.println(ADMINISTRATOR_MENU);
			System.out.println(ASK_FOR_OPERATION);
			System.out.println(ADMINISTRATOR_MENU_LIST);
			System.out.print(ENTER_CHOICE);
			if (sc.hasNextInt()){
    			
    			switch (sc.nextInt()){
    			case 1: 
    				Administrator.createTable();
    				System.out.println(CREATE_TABLE_COMPLETE);
    				break;
    			case 2: 
    				Administrator.deleteTable();
    				System.out.println(DELETE_TABLE_COMPLETE);
    				break;
    			case 3:
    				System.out.print(ASK_FOR_PATH);
    				if (sc.hasNext()){
    					Administrator.loadData(sc.next());
    					System.out.println();
    				} else {
    					System.out.println();
    					System.out.println(ASK_FOR_PATH);
    					sc.next();
    				}
    				break;
    			case 4:
    				result = Administrator.showNoOfRecords();
    				System.out.println(result);
    				System.out.println();
    			case 5:
    				return;
    				
    			default:
    				System.out.println(INVALID_OPERATION);
    			
    			}
    		} else {
    			System.out.println(INVALID_OPERATION);
    			sc.next();
    		}
			
			
		}
		
	}
    public static void salesperson() throws SQLException{
        Scanner sc = new Scanner(System.in);
        int type = 0;
        int order = 0;
        String keyword = null;
		
		while (true){
			
			System.out.println(SALESPERSON_MENU);
			System.out.println(ASK_FOR_OPERATION);
			System.out.println(SALESPERSON_LIST);
			System.out.print(ENTER_CHOICE);
			if (sc.hasNextInt()){
    			
    			switch (sc.nextInt()){
    			case 1: 
    				
    				System.out.println(CHOOSE_SEARCH_CRITERION);
    				System.out.println(SEARCH_CRITERION);
    				System.out.print(CHOOSE_SEARCH_CRITERION);
    				if (sc.hasNextInt()){
    					type = sc.nextInt();
    					while (type !=1 && type !=2 ){
    						System.out.println(INVALID_ORDER);
    						System.out.print(CHOOSE_SEARCH_CRITERION);
    						if (sc.hasNextInt()){
    							type = sc.nextInt();
    						} else {
    							System.out.println(INVALID_TYPE);
    							System.out.print(CHOOSE_SEARCH_CRITERION);
    						}
    					}
    				} else {
    					System.out.print(CHOOSE_SEARCH_CRITERION);
    				}
    				System.out.print(ASK_FOR_KEYWORD);
    				if (sc.hasNext()){
    					keyword = sc.next();
    					
    				} else {
    					
    					System.out.print(ASK_FOR_KEYWORD);
    				}
    				System.out.println(CHOOSE_ORDER + "\n" + ORDER_LIST);
    				System.out.print(CHOOSE_SEARCH_CRITERION);
    				if (sc.hasNextInt()){
    					order = sc.nextInt();
    					while (order !=1 && order !=2 ){
    						System.out.println(INVALID_ORDER);
    						System.out.print(CHOOSE_SEARCH_CRITERION);
    						if (sc.hasNextInt()){
    							order = sc.nextInt();
    							
    							
    						} else {
    							System.out.print(CHOOSE_SEARCH_CRITERION);
    						}
    					}
    				} else {
    					System.out.print(CHOOSE_SEARCH_CRITERION);
    				}
    				String re = Salesperson.SearchStock(keyword,type, order);
    				System.out.println(re);
    				break;
    			case 2: 
    				int partid = 0;
    				int saleid = 0;
    				System.out.print(ASK_FOR_PARTID);
    				while (partid == 0){
    					if (sc.hasNextInt()){
    						partid = sc.nextInt();
    						
    					} else {
    						System.out.print(ASK_FOR_PARTID);
    					}
    				}
    				System.out.print(ASK_FOR_SALPID);
    				while (saleid == 0){
    					if (sc.hasNextInt()){
    						saleid = sc.nextInt();
    						System.out.println();
    					} else {
    						System.out.println();
    						System.out.print(ASK_FOR_SALPID);
    					}
    				}
    				
    				returnMessage = Salesperson.MakeTransaction(partid, saleid);
    				System.out.println(returnMessage);
    				break;
    			case 3:
    				return;
    				
    			default:
    				System.out.println(INVALID_OPERATION);
    			
    			}
    		} else {
    			System.out.println(INVALID_OPERATION);
    			sc.next();
    		}	
		}
		
	}
    public static void manager() throws SQLException{
        Scanner sc = new Scanner(System.in);
		int choice = 0;
		int lower = -1; 
		int upper = -1; 
		while (true){
			
			System.out.println(MANAGER_MENU);
			System.out.println(ASK_FOR_OPERATION);
			System.out.println(MANAGER_LIST);
			System.out.print(ENTER_CHOICE);
			if (sc.hasNextInt()){
    			
    			switch (sc.nextInt()){
    			case 1: 
    				System.out.print(LOWER_EXPERIENCE);
    				while (lower == -1){
    					if (sc.hasNextInt()){
    						lower = sc.nextInt();
    					} else {
    	    				System.out.print(LOWER_EXPERIENCE);
    					}
    				}
    				System.out.print(UPPER_EXPERIENCE);
    				while (upper == -1){
    					if (sc.hasNextInt()){
    						upper = sc.nextInt();
    						
    					} else {
    						
    	    				System.out.print(UPPER_EXPERIENCE);
    					}
    				}
    				System.out.println();
    				
    			//	Manager.countNoOfRecordBasedOnExperneice(lower, upper);
    				break;
    			case 2: 
    			//	Manager.ShowTotalSalesOfEachManufactuerer();
    				break;
    			case 3:
    				int noOfParts = 0;
    				
    				System.out.print(TYPE_NO_OF_PARTS);
    				while (noOfParts == 0){
    					if (sc.hasNextInt()){
    						noOfParts = sc.nextInt();
    					
    					} else {
    						
    	    				System.out.print(TYPE_NO_OF_PARTS);
    					}
    				}
    			//	Manager.ShowNMostPopular(noOfParts);
    				break;
    			case 4:
    				return;
    				
    			default:
    				System.out.println(INVALID_OPERATION);
    			
    			}
    		} else {
    			System.out.println(INVALID_OPERATION);
    			sc.next();
    		}
			
			
		}
		
	}
}
