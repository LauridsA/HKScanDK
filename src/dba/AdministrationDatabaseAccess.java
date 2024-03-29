package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.DbaException;
import model.DailyMessages;
import model.ProductionStop;
import model.Team;

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
	 * @throws DbaException 
	 * @throws Exception
	 */
	public void createDailyMessage(String message, Long timestamp, Long expire, Long showDate) throws DbaException {
		PreparedStatement statement = null;
		String query = "INSERT INTO dailymessages(dmessage, dtimestamp, expire, showdate) VALUES (?, ?, ?, ?);";
		
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			statement.setString(1, message);
			statement.setLong(2, timestamp);
			statement.setLong(3, expire);
			statement.setLong(4, showDate);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbaException("Database fejl: DailyMessage kunne ikke oprettes.", e);
		} finally {
			DBConnection.getInstance().closeConnection();
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
	 * @throws DbaException 
	 */
	public void updateDailyMessage(int id, String newMessage, Long newTimeStamp, Long newExpire, Long newShowDate) throws DbaException {
		PreparedStatement statement = null;
		String query = "UPDATE dailymessages SET dmessage=?, dtimestamp=?, expire=?, showdate=? WHERE id = ?;";
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(5, id);
			statement.setString(1, newMessage);
			statement.setLong(2, newTimeStamp);
			statement.setLong(3, newExpire);
			statement.setLong(4, newShowDate);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbaException("Database fejl: Kunne ikke opdatere DailyMessage.", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}
		
	}
	
	/**
	 * Used to delete a daily message from the dailymessages table in the database.
	 * @param id the id from UI used to find the dailyMessage in the dailymessages table.
	 * @throws DbaException 
	 */
	public void deleteDailyMessage(int id) throws DbaException {
		PreparedStatement statement = null;
		String query = "DELETE FROM dailymessages WHERE id = ?;";
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbaException("Database fejl: DailyMessage kunne ikke slettes.", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}
		
	}
	
	/**
	 * Used to get all daily messages currently in the database, ordered by display date ascendingly.<br>
	 * @return an arrayList of dailyMessages to be used and displayed in UI.
	 * @throws DbaException 
	 */
	public ArrayList<DailyMessages> getAllMessages() throws DbaException {
		PreparedStatement statement = null;
		String query = "SELECT dmessage, dtimestamp, expire, showdate FROM dailymessages SORT BY showdate ASC";
		ResultSet result = null;
		ArrayList<DailyMessages> messageList = new ArrayList<>();
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
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
		} catch (SQLException e) {
			throw new DbaException("Database fejl: Der blev ikke fundet nogen dailymessages.", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}
		
		return messageList;
	}
	
	/**
	 * Used to create a production stop from administration.
	 * @param stopTime Unix time stamp at which this stop occurred.
	 * @param stopLength Length of the stop in minutes.
	 * @param stopDescription String displaying a description of the stop.
	 * @param team Team object used for tying together and displaying information.
	 * @return 
	 * @throws DbaException 
	 */
	@SuppressWarnings("static-access")
	public ProductionStop createStop(Long stopTime, int stopLength, String stopDescription, Team team) throws DbaException {
		PreparedStatement statement = null;
		String query = "DECLARE @teamtimetableid INT = ?; INSERT INTO productionstop(stoptime, stoplength, stopdescription, teamtimetableid) VALUES (?, ?, ?, @teamtimetableid);";
		int keyId = 0;
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query, statement.RETURN_GENERATED_KEYS); // static-access suppressed.
			statement.setLong(2, stopTime);
			statement.setInt(3, stopLength);
			statement.setString(4, stopDescription);
			statement.setInt(1, team.getTeamId());
			
			statement.executeUpdate();
	/*
		        if (result.equals(null)) {
		            throw new DbaException("Opretning af stop fejlet, ingen r�kker blev p�virket.");
		        }
	*/
		        ResultSet generatedKeys = statement.getGeneratedKeys(); 
		            if (generatedKeys.next()) {
		                keyId = generatedKeys.getInt(1);
		            }
		            else {
		                throw new DbaException("Opretning af stop fejlet, der blev ikke fanget noget ID.");
		            }
		        
		  
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new DbaException("Fejl i database", e1);
			}
				throw new DbaException("Productionsstop ikke oprettet", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}

		return new ProductionStop(keyId, stopTime, stopLength, stopDescription, team);
		
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
	 * @throws DbaException 
	 */
	public void updateStop(int id, Long newStopTime, int newStopLength, String newStopDescription, Team team) throws DbaException {
		PreparedStatement statement = null;
		String query = "UPDATE productionstop SET stoptime=?, stoplength=?, stopdescription=?, teamtimetableid=? WHERE id=?;";
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(5, id);
			statement.setLong(1, newStopTime);
			statement.setInt(2, newStopLength);
			statement.setString(3, newStopDescription);
			statement.setLong(4, team.getTeamId());
			statement.executeUpdate();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new DbaException("Database Fejl: Fejl undervejs i processen. Tjek forbindelse.", e);
			}
			throw new DbaException("Database Fejl: Productionsstop kunne ikke opdateres", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}
	}
	
	/**
	 * Used to delete a ProductionStop from the productionstop table in database.
	 * Uses id to find the productionStop to delete.
	 * @param id the id from UI used to find the productionStop in the productionstop table.
	 * @throws DbaException 
	 */
	public void deleteStop(int id) throws DbaException {
		PreparedStatement statement = null;
		String query = "DELETE FROM productionstop WHERE id=?";
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			System.out.println(id);
			System.out.println(statement.executeUpdate());
			
		} catch (SQLException e) {
			throw new DbaException("Database Fejl: Productionsstop kunne ikke slettes", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}
		
	}
}

