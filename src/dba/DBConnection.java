package dba;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DbaException;

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
	 * @throws DbaException 
	 */
	private DBConnection() throws DbaException {
		String connectionString = "jdbc:sqlserver://" + server + ";databaseName=" + databaseName + ";user=" + userName + ";password=" + passWord;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionString);
			con.setAutoCommit(true);
			dma = con.getMetaData();
		} catch (SQLException e) {
			System.out.println("Con problem");
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new DbaException("Driver ikke fundet", e);
		}
	}
	
	/**
	 * Used to close the current connection.<br>
	 * Resets the singleton connection.
	 * @throws DbaException 
	 */
	public void closeConnection() throws DbaException {
		try {
			con.close();
			instance = null;
		} catch (SQLException e) {
			throw new DbaException("Forbindelse kunne ikke lukkes", e);
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
	 * @throws DbaException 
	 */
	public static DBConnection getInstance() throws DbaException {
		if(instance == null){
			instance = new DBConnection();
		}
		return instance;
	}
}
