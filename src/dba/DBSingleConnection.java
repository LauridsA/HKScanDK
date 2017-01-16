package dba;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DbaException;

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
	 * @throws DbaException 
	 */
	private void openConnection() throws DbaException {
		String connectionString = "jdbc:sqlserver://" + server + ";databaseName=" + databaseName + ";user=" + userName + ";password=" + passWord;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionString);
			con.setAutoCommit(true);
			dma = con.getMetaData();
		} catch (SQLException e) {
			throw new DbaException("Kunne ikke oprette forbindelse til database", e);
		} catch (ClassNotFoundException e) {
			throw new DbaException("Driver ikke fundet", e);
		}

	}
	
	/**
	 * Used to close the current connection.<br>
	 * Releases the connection for use by other threads.
	 * @throws DbaException 
	 */
	public synchronized void closeConnection() throws DbaException {
		try {
			con.close();
			inuse = false;
			notifyAll();
		} catch (SQLException e) {
			throw new DbaException("Forbindelse kunne ikke lukkes", e);
		}
	}
	
	/**
	 * Synchronized method used to retrieve the current database connection session.<br>
	 * If the current connection is in use it will call for the thread to wait.
	 * @return Connection session.
	 * @throws DbaException 
	 */
	public synchronized Connection getDBcon() throws DbaException {
		while (inuse) {
			try {
				System.out.println("I'm waiting");
				wait();
			} catch (InterruptedException e) {
				throw new DbaException("Fejl i tråd", e);
			}
		}
		inuse = true;
		openConnection();
		notifyAll();
		return con;
		
	}
}
