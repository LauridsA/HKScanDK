package dba;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class DBConnection {

	private static final String server = "aur.dk";
	private static final String databaseName = "UCN_dmaa0216_2Sem_1";
	private static final String userName = "UCN_dmaa0216_2Sem_1";
	private static final String passWord = "Password1!";
	
	@SuppressWarnings("unused")
	private DatabaseMetaData dma;
	private static Connection con;
	
	private static DBConnection instance = null;
	
	
	/**
	 * Private constructor. Immediately connects to the database.
	 */
	private DBConnection() {
		String connectionString = "jdbc:sqlserver://" + server + ";databaseName=" + databaseName + ";user=" + userName + ";password=" + passWord;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (Exception e) {
			System.out.println("Driver not found");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}
		
		try {
			con = DriverManager.getConnection(connectionString);
			con.setAutoCommit(true);
			dma = con.getMetaData();
		} catch (Exception e) {
			System.out.println("Con problem");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to close the current connection.<br>
	 * Resets the singleton connection.
	 */
	public void closeConnection() {
		try {
			con.close();
			instance = null;
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves the current session.
	 * @return Connection session.
	 */
	public Connection getDBcon()
	{
		return con;
	}
	
	/**
	 * Method for singleton construction.
	 * @return singleton instance.
	 */
	public static DBConnection getInstance() {
		if(instance == null){
			instance = new DBConnection();
		}
		return instance;
	}
}
