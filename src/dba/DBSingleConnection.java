package dba;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class DBSingleConnection {

	private static final String server = "kraka.ucn.dk";
	private static final String databaseName = "UCN_dmaa0216_2Sem_1";
	private static final String userName = "UCN_dmaa0216_2Sem_1";
	private static final String passWord = "Password1!";
	
	private DatabaseMetaData dma;
	private static Connection con;
	private boolean inuse = false;
	
	
	public DBSingleConnection() {
		
	}
	
	private void openConnection() {
		String connectionString = "jdbc:sqlserver://" + server + ";databaseName=" + databaseName + ";user=" + userName + ";password=" + passWord;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//System.out.println("Driver find the driver");
		} catch (Exception e) {
			System.out.println("Driver not found");
			System.out.println(e.getMessage());
			
		}
		
		try {
			con = DriverManager.getConnection(connectionString);
			con.setAutoCommit(true);
			dma = con.getMetaData();
			//System.out.println("connection suceded");
		} catch (Exception e) {
			System.out.println("Con problem");
			System.out.println(e.getMessage());
		}

	}
	
	
	
	public void closeConnection() {
		try {
			con.close();
			inuse = false;
			System.out.println("everyone wake up");
			notifyAll();
		} catch (Exception e) {
			System.out.println("error");
		}
	}
	
	public Connection getDBcon()
	{
		while (inuse) {
			try {
				System.out.println("I'm waiting");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		inuse = true;
		openConnection();
		return con;
	}
}
