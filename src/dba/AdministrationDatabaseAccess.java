package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.DailyMessages;

public class AdministrationDatabaseAccess {
	private DBSingleConnection dbSinCon;
	
	public AdministrationDatabaseAccess(DBSingleConnection dbSinCon) {
		this.dbSinCon = dbSinCon;
	}
	
	public AdministrationDatabaseAccess() {
		// TODO Auto-generated constructor stub
	}
	
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
	public void createDailyMessage(String message, Long timestamp, Long expire, Long showDate) {
		PreparedStatement statement = null;
		String query = "INSERT INTO dailymessages(dmessage, dtimestamp, expire, showdate) VALUES (?, ?, ?, ?);";
		
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setString(1, message);
			statement.setLong(2, timestamp);
			statement.setLong(3, expire);
			statement.setLong(4, showDate);
			statement.executeUpdate();
			con.commit();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				System.out.println("Database Error: DailyMessage not created.");
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e){					
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Used to update an existing dailyMessage. <br>
	 * Takes parameters from UI and inserts them into the dailymessages table in the database at specified id.<br>
	 * If none of the fields are changed it simply re-inserts the old data.
	 * @param id the id from UI used to find the dailyMessage in the dailymessages table
	 * @param newMessage the new string to display
	 * @param newTimeStamp new Unix time stamp to use
	 * @param newExpire new expiration date
	 * @param newShowDate new time to display the message
	 */
	public void updateDailyMessage(int id, String newMessage, Long newTimeStamp, Long newExpire, Long newShowDate) {
		PreparedStatement statement = null;
		String query = "UPDATE dailymessages SET dmessage=?, dtimestamp=?, expire=?, showdate=? WHERE id = ?;";
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setInt(5, id);
			statement.setString(1, newMessage);
			statement.setLong(2, newTimeStamp);
			statement.setLong(3, newExpire);
			statement.setLong(4, newShowDate);
			statement.executeUpdate();
			con.commit();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				System.out.println("Database Error: DailyMessage not found.");
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	public void deleteDailyMessage(int id) {
		PreparedStatement statement = null;
		String query = "DELETE FROM dailymessages WHERE id = ?;";
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			con.commit();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				System.out.println("Database Error: DailyMessage not found.");
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	public ArrayList<DailyMessages> getAllMessages() {
		PreparedStatement statement = null;
		String query = "SELECT dmessage, dtimestamp, expire, showdate FROM dailymessages;";
		ResultSet result = null;
		ArrayList<DailyMessages> messageList = new ArrayList<>();
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()){
				while(result.next()){
					String message = result.getString("dmessage");
					Long timestamp = result.getLong("dtimestamp");
					Long expire = result.getLong("expire");
					Long showDate = result.getLong("showdate");
					
					DailyMessages dm = new DailyMessages(message, timestamp, expire, showDate);
					messageList.add(dm);
				}
			}
			
		} catch (Exception e) {
			System.out.println("Database Error: Found nothing.");
			System.out.println(e.getMessage());
		} finally {
			dbSinCon.closeConnection();
		}
		
		return messageList;
	}
	
	
}
