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
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setString(1, message);
			statement.setLong(2, timestamp);
			statement.setLong(3, expire);
			statement.setLong(4, showDate);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbaException("Database fejl: DailyMessage kunne ikke oprettes.", e);
		} finally {
			dbSinCon.closeConnection();
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
			con = dbSinCon.getDBcon();
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
			dbSinCon.closeConnection();
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
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbaException("Database fejl: DailyMessage kunne ikke slettes.", e);
		} finally {
			dbSinCon.closeConnection();
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
		} catch (SQLException e) {
			throw new DbaException("Database fejl: Der blev ikke fundet nogen dailymessages.", e);
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
	 * @throws DbaException 
	 */
	public ProductionStop createStop(Long stopTime, int stopLength, String stopDescription, int teamTimeTableId) throws DbaException {
		PreparedStatement statement = null;
		String query = "INSERT INTO productionstop(stoptime, stoplength, stopdescription, teamtimetableid) VALUES (?, ?, ?, ?)";
		int id = 0;
		
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query, statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, stopTime);
			statement.setInt(2, stopLength);
			statement.setString(3, stopDescription);
			statement.setInt(4, teamTimeTableId);
			
			int affectedRows = statement.executeUpdate();
	
		        if (affectedRows == 0) {
		            throw new DbaException("Opretning af stop fejlet, ingen rækker blev påvirket.");
		        }
	
		        ResultSet generatedKeys = statement.getGeneratedKeys(); 
		            if (generatedKeys.next()) {
		                id = generatedKeys.getInt(1);
		            }
		            else {
		                throw new DbaException("Opretning af stop fejlet, der blev ikke fanget noget ID.");
		            }
		        			
			
		} catch (SQLException e) {
				throw new DbaException("Productionsstop ikke oprettet", e);
		} finally {
			DBConnection.getInstance().closeConnection();
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
	 * @throws DbaException 
	 */
	public void updateStop(int id, Long newStopTime, int newStopLength, String newStopDescription, int newTeamTimeTableId) throws DbaException {
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
			statement.setLong(4, newTeamTimeTableId);
			statement.executeUpdate();
			
		} catch (SQLException e) {
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
		String query = "DELETE FROM productionstop WHERE id=?;";
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbaException("Database Fejl: Productionsstop kunne ikke slettes", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}
		
	}
	
	/**
	 * Used to retrieve all ProductioStops from database.
	 * @return all DailyMessages from the productionstop table as ArrayList.
	 * @throws DbaException 
	 */
	public ArrayList<ProductionStop> getAllStops() throws DbaException {
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
					int teamId = result.getInt("id");
					Long startTime = result.getLong("starttimestamp");
					Long endTime = result.getLong("endtimestamp");
					String teamName = result.getString("teamname");
					int teamSize = result.getInt("workers");
					int department = result.getInt("department");
					Team team = new Team(teamId, startTime, endTime, teamName, teamSize, department);
					ProductionStop ps = new ProductionStop(id, st, sl, sd, ttti, team);
					stopList.add(ps);
				}
			}
			
		} catch (SQLException e) {
			throw new DbaException("Database Fejl: kunne ikke finde alle productionstop", e);
		} finally {
			DBConnection.getInstance().closeConnection();
		}
		
		return stopList;
	}
}
