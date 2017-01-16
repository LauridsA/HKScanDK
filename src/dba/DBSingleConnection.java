package dba;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class DBSingleConnection {

	private static final String server = "aur.dk";
	private static final String databaseName = "UCN_dmaa0216_2Sem_1";
	private static final String userName = "UCN_dmaa0216_2Sem_1";
	private static final String passWord = "Password1!";
	
	@SuppressWarnings("unused")
	private DatabaseMetaData dma;
	private static Connection con;
	private boolean inuse = false;
	
	
	public DBSingleConnection() {
		// Empty constructor.
	}
	
	/**
	 * Private method used to establish connection the database.
	 */
	private void openConnection() {
		String connectionString = "jdbc:sqlserver://" + server + ";databaseName=" + databaseName + ";user=" + userName + ";password=" + passWord;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (Exception e) {
			System.out.println("Driver not found");
			e.printStackTrace();
			
		}
		
		try {
			con = DriverManager.getConnection(connectionString);
			con.setAutoCommit(true);
			dma = con.getMetaData();
		} catch (Exception e) {
			System.out.println("Con problem");
			e.printStackTrace();
		}

	}
	
	/**
	 * Used to close the current connection.<br>
	 * Releases the connection for use by other threads.
	 */
	public synchronized void closeConnection() {
		try {
			con.close();
			inuse = false;
			notifyAll();
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}
	
	/**
	 * Synchronized method used to retrieve the current database connection session.<br>
	 * If the current connection is in use it will call for the thread to wait.
	 * @return Connection session.
	 */
	public synchronized Connection getDBcon() {
		while (inuse) {
			try {
				System.out.println("I'm waiting");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		inuse = true;
		openConnection();
		notifyAll();
		return con;
		
	}
}
