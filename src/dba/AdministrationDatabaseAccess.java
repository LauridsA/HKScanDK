package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.DailyMessages;
import model.ProductionStop;

public class AdministrationDatabaseAccess {
	private DBSingleConnection dbSinCon;
	
	public AdministrationDatabaseAccess(DBSingleConnection dbSinCon) {
		this.dbSinCon = dbSinCon;
	}
	
	public AdministrationDatabaseAccess() {
		//empty constructor
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
	
	/**
	 * Used to delete a daily message from the dailymessages table in the database.
	 * @param id the id from UI used to find the dailyMessage in the dailymessages table.
	 */
	public void deleteDailyMessage(int id) {
		PreparedStatement statement = null;
		String query = "DELETE FROM dailymessages WHERE id = ?;";
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
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
	
	/**
	 * Used to get all daily messages currently in the database, ordered by display date ascendingly.<br>
	 * @return an arrayList of dailyMessages to be used and displayed in UI.
	 */
	public ArrayList<DailyMessages> getAllMessages() {
		PreparedStatement statement = null;
		String query = "SELECT dmessage, dtimestamp, expire, showdate FROM dailymessages SORT BY showdate ASC";
		ResultSet result = null;
		ArrayList<DailyMessages> messageList = new ArrayList<>();
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
				while(result.next()){
					String message = result.getString("dmessage");
					Long timestamp = result.getLong("dtimestamp");
					Long expire = result.getLong("expire");
					Long showDate = result.getLong("showdate");
					
					DailyMessages dm = new DailyMessages(message, timestamp, expire, showDate);
					messageList.add(dm);
				}
			
		} catch (Exception e) {
			System.out.println("Database Error: Found nothing.");
			System.out.println(e.getMessage());
		} finally {
			dbSinCon.closeConnection();
		}
		
		return messageList;
	}
	
	/**
	 * Used to create a production stop from administration.
	 * @param stopTime Unix time stamp at which this stop occurred.
	 * @param stopLength Length of the stop in minutes.
	 * @param stopDescription String displaying a description of the stop.
	 * @param TeamTimeTableId ID of timetable working at the time of stop.
	 * @return 
	 */
	public ProductionStop createStop(Long stopTime, int stopLength, String stopDescription, int teamTimeTableId) {
		PreparedStatement statement = null;
		String query = "INSERT INTO productionstop(stoptime, stoplength, stopdescription, teamtimetableid) VALUES (?, ?, ?, ?)";
		int id = 0;
		
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			statement.setLong(1, stopTime);
			statement.setInt(2, stopLength);
			statement.setString(3, stopDescription);
			statement.setInt(4, teamTimeTableId);
			statement.executeUpdate();
			int affectedRows = statement.executeUpdate();
	
		        if (affectedRows == 0) {
		            throw new SQLException("Creating user failed, no rows affected.");
		        }
	
		        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		                id = generatedKeys.getInt(1);
		            }
		            else {
		                throw new SQLException("Creating user failed, no ID obtained.");
		            }
		        }
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				System.out.println("Database Error: ProductionStop not created.");
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.getInstance().closeConnection();
			} catch (SQLException e){					
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		return new ProductionStop(id, stopTime, stopLength, stopDescription, teamTimeTableId);
		
	}
	
	/**
	 * Used to update an existing ProductionStop. <br>
	 * Takes parameters from UI and inserts them into the productionstop table in the database at specified id.<br>
	 * If none of the fields are changed it simply re-inserts the old data.
	 * @param id the id from UI used to find the productionStop in the productionstop table
	 * @param newStopTime the new Unix time stamp
	 * @param newStopLength new int for stopLength
	 * @param newStopDescription new String to display
	 * @param newTeamTimeTableId new int for teamTimeTable
	 */
	public void updateStop(int id, Long newStopTime, int newStopLength, String newStopDescription, int newTeamTimeTableId) {
		PreparedStatement statement = null;
		String query = "UPDATE productionstop SET stoptime=?, stoplength=?, stopdescription=?, teamtimetableid=? WHERE id=?;";
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setInt(5, id);
			statement.setLong(1, newStopTime);
			statement.setInt(2, newStopLength);
			statement.setString(3, newStopDescription);
			statement.setLong(4, newTeamTimeTableId);
			statement.executeUpdate();
			con.commit();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				System.out.println("Database Error: ProductionStop not found.");
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.getInstance().closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Used to delete a ProductionStop from the productionstop table in database.
	 * Uses id to find the productionStop to delete.
	 * @param id the id from UI used to find the productionStop in the productionstop table.
	 */
	public void deleteStop(int id) {
		PreparedStatement statement = null;
		String query = "DELETE FROM productionstop WHERE id=?;";
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			con.commit();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				System.out.println("Database Error: ProductionStop not found.");
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.getInstance().closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Used to retrieve all ProductioStops from database.
	 * @return all DailyMessages from the productionstop table as ArrayList.
	 */
	public ArrayList<ProductionStop> getAllStops() {
		PreparedStatement statement = null;
		String query = "SELECT id, stoptime, stoplength, stopdescription, teamtimetableid FROM productionstop ORDER BY stoptime desc";
		ResultSet result = null;
		ArrayList<ProductionStop> stopList = new ArrayList<>();
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()){
				while(result.next()){
					int id = result.getInt("id");
					Long st = result.getLong("stoptime");
					int sl = result.getInt("stoplength");
					String sd = result.getString("stopdescription");
					int ttti = result.getInt("teamtimetableid");
					
					ProductionStop ps = new ProductionStop(id, st, sl, sd, ttti);
					stopList.add(ps);
				}
			}
			
		} catch (Exception e) {
			System.out.println("Database Error: Found nothing.");
			System.out.println(e.getMessage());
		} finally {
			DBConnection.getInstance().closeConnection();
		}
		
		return stopList;
	}
}
