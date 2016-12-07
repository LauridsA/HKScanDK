package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.DailyMessages;

public class AdministrationDatabaseAccess {
	private DBSingleConnection dbSinCon;
	
	/**
	 * Takes parameters from UI and inserts them into the dailymessages table in the database. <br> 
	 * Creates a new DailyMessage to be displayed.
	 * @param message the string to be displayed.
	 * @param timestamp generated at call, system Unix timestamp.
	 * @param expire the expiration date for this message.
	 * @param showDate the time at which this message should be displayed from.
	 * @return DailyMessages
	 * @throws Exception
	 */
	public DailyMessages createDailyMessage(String message, Long timestamp, Long expire, Long showDate) throws Exception {
		PreparedStatement statement = null;
		String query = "INSERT INTO dailymessages(dmessage, dtimestamp, expire, showdate) VALUES (?, ?, ?, ?);";
		DailyMessages dailyMessage = null;
		ResultSet result;
		
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, message);
			statement.setLong(2, timestamp);
			statement.setLong(3, expire);
			statement.setLong(4, showDate);
			int exe = statement.executeUpdate();
			con.commit();
			
			if(exe > 0){
				result = statement.getGeneratedKeys();
				if(result.next()){
					 dailyMessage = new DailyMessages(message, timestamp, expire, showDate);
					 System.out.println("DailyMessage created.");
				}
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			con.rollback();
			throw new Exception("Database Error: DailyMessage not created.");
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e){					
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		return dailyMessage;
	}

}
