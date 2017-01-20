package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import exceptions.DbaException;
import model.DailyMessages;
import model.FieldTypes;
import model.ProductionStop;
import model.Team;
import model.WorkingTeam;

public class DatabaseAccess {
	private DBSingleConnection dbSinCon;
	
	public DatabaseAccess(DBSingleConnection dbSinCon) {
		this.dbSinCon = dbSinCon;
	}
	
	public DatabaseAccess() {
		// Empty constructor
	}
	
	/**
	 * Used to retrieve whether any of the batches are organic or not, based on current working teamtimetableid.
	 * @param WorkingTeam id of the current working team.
	 * @return True, if any of the days' batches are organic.
	 * @throws DbaException 
	 */
	public Boolean getOrganic(int teamid) throws DbaException{
		PreparedStatement statement = null;
		String query = "DECLARE @teamid INT = ?; SELECT organic FROM batch WHERE (teamnighttimetableid = @teamid OR teamdaytimetableid = @teamid) AND organic = 1";
		ResultSet result = null;
		Connection con = null;
		Boolean organic = false;
		
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(1, teamid);
			result = statement.executeQuery();
			con.commit();
			if(!result.isBeforeFirst()) {
				organic = false;
			} else {
				organic = true;
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				try {
					con.setAutoCommit(true);
					dbSinCon.closeConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return organic;
	}

	/**
	 * Retrieves the average weight of the current batch from the database.
	 * @param fromTimeDate the start time of the desired average weight
	 * @param toTimeDate the end time of the desired average weight
	 * @return the average weight as an int
	 * @throws DbaException 
	 */
	public int getAvgWeight() throws DbaException {
		PreparedStatement statement = null;
		String query = "SELECT avgweight FROM batch WHERE batch.id = (SELECT TOP 1 batchid FROM slaughteramount ORDER BY satimestamp DESC)";
		ResultSet result = null;
		Connection con = null;
		int avgweight = 0;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			while (result.next()) {
				avgweight = result.getInt("avgweight");
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return avgweight;
	}

	/**
	 * Retrieves the speed value between two given times.<br>
	 * Mostly an unused relic from an earlier iteration.
	 * @param fromTimeStamp the start time of the desired speed.
	 * @param toTimeStamp the end time of the desired speed.
	 * @return speed as an integer number.
	 * @throws DbaException 
	 */
	public int getAvgSpeed(long fromTimeStamp, long toTimeStamp) throws DbaException {
		PreparedStatement statement = null;
		String query = "SELECT value FROM speed WHERE stimestamp BETWEEN ? AND ?";
		ResultSet result = null;
		int speed = 0;
		
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(1, fromTimeStamp);
			statement.setLong(2, toTimeStamp);
			result = statement.executeQuery();
			con.commit();
			while (result.next()) {
				speed += result.getInt("value");
			}
			speed = speed / result.getFetchSize();
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		if (speed == 0) {
			throw new DbaException("Data kunne ikke findes");
		}
		return speed;
	}
	
	/**
	 * Retrieves the most recent speed value from the database.
	 * @return current speed as integer.
	 * @throws DbaException 
	 */
	public int getCurrentSpeed() throws DbaException {
		PreparedStatement statement = null;
		String query = "SELECT * FROM currentspeedview";
		ResultSet result = null;
		int speed = 0;
		
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			con.commit();
			if(result.isBeforeFirst()) {
				result.next();
				speed = result.getInt("value");
			} else {
				speed = 0;
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		if (speed == 0) {
			//This might be changed later, as the speed can be 0 without database errors
			throw new DbaException("Data kunne ikke findes");
		}
		return speed;
	}
	
	/**
	 * Retrieves a refresh rate value for a given field.
	 * @param type Takes the parameter of enumerate FieldTypes, which can determine which refresh rate to return.
	 * @return Returns the refresh rate of the specified field as an integer.
	 * @throws DbaException 
	 */
	public int getRefreshRate(FieldTypes type) throws DbaException {
		String sqlType;
		switch (type) {
		case SPEED:
			sqlType = "SPEED";
			break;
		case AVGWEIGHT:
			sqlType = "AVGWEIGHT";
			break;
		case ORGANIC:
			sqlType = "ORGANIC";
			break;
		case SLAUGTHERAMOUNTNIGHT:
			sqlType = "SLAUGTHERAMOUNTNIGHT";
			break;
		case SLAUGTHERAMOUNTDAY:
			sqlType = "SLAUGTHERAMOUNTDAY";
			break;
		case STOPNIGHT:
			sqlType = "STOPNIGHT";
			break;
		case STOPDAY:
			sqlType = "STOPDAY";
			break;
		case DAYEXPECTED:
			sqlType = "DAYEXPECTED";
			break;
		case EXPECTEDFINISH:
			sqlType = "EXPECTEDFINISH";
			break;
		case TOTALSLAUGTHERAMOUNT:
			sqlType = "TOTALSLAUGTHERAMOUNT";
			break;
		case PRODUCTIONSTOPS:
			sqlType = "PRODUCTIONSTOPS";
			break;
		case DAILYMESSAGES:
			sqlType = "DAILYMESSAGES";
			break;
		case WORKINGTEAM:
			sqlType = "WORKINGTEAM";
			break;
		case CURRENTSLAUGHTERAMOUNT:
			sqlType = "CURRENTSLAUGHTERAMOUNT";
			break;
		default:
			return 5;
		}
		String query = "SELECT refreshrate FROM information WHERE informationname = ?";
		PreparedStatement statement;
		ResultSet result = null;
		int res = 0;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setString(1, sqlType);
			result = statement.executeQuery();
			if(result.isBeforeFirst()) {
				result.next();
				res = result.getInt("refreshrate");
			} else {
				res = 10;
			}
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
	    	dbSinCon.closeConnection();
	    }
		return res;
	}
	
	/**
	 * Retrieves and sets teamId, teamTimeTableId, startTime, and endTime for class WorkingTeam at given time stamp.
	 * @param time Unix time stamp.
	 * @throws DbaException 
	 */
	public void getCurrentWorkingTeam(long time) throws DbaException{
		
		PreparedStatement statement = null;
		String query = "SELECT * FROM workingteamfunction(?)";
		ResultSet result = null;		
		int teamTimeTableId = 0;
		long startTime = 0;
		long endTime = 0;
		int teamId = 0;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setLong(1, time);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()){
				result.next();
				teamTimeTableId = result.getInt("id");
				startTime = result.getLong("starttimestamp");
				endTime = result.getLong("endtimestamp");
				teamId = result.getInt("teamid");
			}
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		WorkingTeam.getInstance().setEverything(teamId, teamTimeTableId, startTime, endTime);
	}
	
	/** 
	 * Retrieves the current slaughtered amount of the night shift.
	 * @param now Specifies the current time in UNIX time stamp.
	 * @return integer of slaughtered night amount.
	 * @throws DbaException 
	 */
	public int getSlaughterAmountNight(long now) throws DbaException {
		PreparedStatement statement = null;
		String query = "DECLARE @now BIGINT = ?; SELECT SUM(value) AS currentamount FROM slaughteramount WHERE teamtimetableid = (SELECT * FROM teamtimetableandteamnightfunction(@now))";
		ResultSet result = null;
		int amountNight = 0;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setLong(1, now);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()) {
				result.next();
				amountNight = result.getInt("currentamount");
			}
			
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return amountNight;
	}
	
	
	/**
	 * Retrieves amount of chickens slaughter by the day team.<br>
	 * Will display 0 if the night team is working.
	 * @param now Specifies the current time in UNIX time stamp.
	 * @return integer of slaughtered day amount.
	 * @throws DbaException 
	 */
	public int getSlaughterAmountDay(long now) throws DbaException {
		PreparedStatement statement = null;
		String query = "DECLARE @now BIGINT = ?; SELECT sum(value) AS currentamount FROM slaughteramount WHERE teamtimetableid = (SELECT * FROM teamtimetableandteamfunction(@now))";
		ResultSet result = null;
		int amountDay = 0;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setLong(1, now);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()) {
				result.next();
				amountDay = result.getInt("currentamount");
			}
			
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return amountDay;
	}
	
	/**
	 * Retrieves the total amount of chickens to be slaughtered by both the day and night team for the current workshift.
	 * @param teamId for specific team.
	 * @return the sum of all rows of slaughteramount matching the given teamtableid.
	 * @throws DbaException 
	 */
	public int	totalSlaughterAmount(int teamId) throws DbaException {
		PreparedStatement statement = null;
		String query = "DECLARE @timetableid int = ?; SELECT SUM(value) AS totalamount FROM batch WHERE teamnighttimetableid = @timetableid OR teamdaytimetableid = @timetableid;";
		ResultSet result = null;
		int expectedAmount = 0;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, teamId);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()) {
				result.next();
				expectedAmount = result.getInt("totalamount");
			}
			
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return expectedAmount;
	}
	
	/**
	 * Retrieves the total amount of chickens slaughter by the current workshift.
	 * @param int teamId.
	 * @return the total slaughtered chickens so far for the working day.
	 * @throws DbaException 
	 */
	public int getTotalCurrentSlaughterAmount(int teamId) throws DbaException{
		PreparedStatement statement = null;
		String query = "DECLARE @team INT = ?; SELECT sum(value) AS totalamount FROM slaughteramount WHERE teamtimetableid = (SELECT * FROM teamnighttimetablefunction(@team)) OR teamtimetableid = (SELECT * FROM teamdaytimetablefunction(@team));";
		ResultSet result = null;
		int totalslaughteredcurrent = 0;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, teamId);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()) {
				result.next();
				totalslaughteredcurrent = result.getInt("totalamount");
			} else{
				System.out.println("FACK OFF");
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return totalslaughteredcurrent;
	}
	
	/**
	 * Retrieves total number of production stops for the night team of the current workshift.
	 * @param teamId.
	 * @return noOfStops as an int for night team.
	 * @throws DbaException 
	 */
	public int getNoStopNight(long now) throws DbaException{
		String query = "DECLARE @now BIGINT = ?; SELECT *  FROM productionstop WHERE teamtimetableid = (SELECT TOP 1 teamtimetable.id FROM teamtimetable JOIN team ON teamtimetable.teamid = team.id WHERE starttimestamp < @now AND teamname = 'nat' ORDER BY starttimestamp DESC);";
		int noOfStops = 0;
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.setLong(1, now);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()) {
				result.last();
				noOfStops = result.getRow();
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return noOfStops;
	}
	
	
	/**
	 * Retrieves total number of production stops for the night team of the current workshift.
	 * @param teamId.
	 * @return number of stops as int for day team.
	 * @throws DbaException 
	 */
	public int getNoStopDay(long now) throws DbaException{
		String query = "DECLARE @now BIGINT = ?; SELECT * FROM productionstop WHERE teamtimetableid = (SELECT TOP 1 teamtimetable.id FROM teamtimetable JOIN team ON teamtimetable.teamid = team.id WHERE starttimestamp < @now AND teamname = 'dag' AND (endtimestamp + 14400000) > @now);";
		int noOfStops = 0;
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.setLong(1, now);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()) {
				result.last();
				noOfStops = result.getRow();
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return noOfStops;
	}
	
	/**
	 * Retrieves the amount of chickens the day team must slaughter to meet the goal for the day.
	 * @param teamId MUST BE DAY TEAM
	 * @return
	 * @throws DbaException 
	 */
	public int dayExpected(int teamId) throws DbaException{
		String query = "DECLARE @team INT = ?; DECLARE @NightTeamAmount INT = (SELECT SUM(value) AS nightamount  FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamnighttimetableid FROM teamtimetable JOIN batch ON teamtimetable.id = batch.teamdaytimetableid WHERE teamdaytimetableid = @team)); SELECT sum(value) - @NightTeamAmount AS result FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamnighttimetableid FROM batch WHERE teamnighttimetableid = @team OR teamdaytimetableid = @team) OR teamtimetableid = (SELECT TOP 1 teamdaytimetableid FROM batch WHERE teamnighttimetableid = @team OR teamdaytimetableid = @team);";
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection con = null;
		int expected = 0;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, teamId);
			result = statement.executeQuery();
			if(!result.isBeforeFirst()) {
				//do nothing
			} else {
				result.next();
				expected = result.getInt("result");
			}
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return expected;
	}

	/**
	 * Calculates and retrieves the amount of chickens expected to be slaughtered per hour.
	 * @param teamId MUST BE DAY TEAM.
	 * @return Integer value expected.
	 * @throws DbaException 
	 */
	public int expectedPerHour(int teamId, long now) throws DbaException {
		String query = "DECLARE @team INT = ?; DECLARE @now BIGINT = ?; DECLARE @totalval INT = (SELECT SUM(value) AS totalval FROM batch WHERE teamdaytimetableid = @team OR teamnighttimetableid = @team) SELECT ISNULL((@totalval-(SELECT SUM(slaughteramount.value) AS slaughterval FROM slaughteramount JOIN batch ON batchid = batch.id WHERE slaughteramount.teamtimetableid = @team)), @totalval)/((((SELECT TOP 1 teamtimetable.endtimestamp FROM batch JOIN teamtimetable ON batch.teamdaytimetableid = teamtimetable.id WHERE batch.teamdaytimetableid = @team OR batch.teamnighttimetableid = @team)) - @now)/3600000) AS result;";
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection con = null;
		int expected = 0;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, teamId);
			statement.setLong(2, now);
			result = statement.executeQuery();
			if(!result.isBeforeFirst()) {
				//do nothing
			} else {
				result.next();
				expected = result.getInt("result");
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return expected;
	}
	
	/**
	 * Calculates and retrieves the expected time at which the workshift will be done with the day's batches.
	 * @param teamId given id of team.
	 * @return A HashMap of k=Integer, v=Integer.<br>
	 * The key holds whether or not the result, held by value, is organic.
	 * @throws DbaException 
	 */
	public Map<Integer, Integer> expectedFinish(int teamId) throws DbaException {
		String query = "SELECT * FROM idvalueorganicbatchfunction(?)";
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection con = null;
		Map<Integer, Integer> total = new HashMap<>();
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setInt(1, teamId);
			result = statement.executeQuery();
			while (result.next()) {
				total.put(result.getInt("organic"), result.getInt("result"));
			}
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
				dbSinCon.closeConnection();
		}
		return total;
	}

	/**
	 * Retrieves the total amount of production stops from the database.
	 * @return Total amount of production stops as an integer.
	 * @throws DbaException 
	 */
	public int getTotalStops() throws DbaException {
	    String query = "SELECT count(id) as total FROM productionstop";
	    Connection con = null;
	    int total = 0;
	    try {
	    con = dbSinCon.getDBcon();
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
			if(result.next()){
			    total = result.getInt("total");
			}
	    } catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
			dbSinCon.closeConnection();
	    }
	    return total;
	}
	
	/**
	 * Used to retrieve all ProductionStops from database.
	 * @return all production stops from the productionstop table as ArrayList.
	 * @throws DbaException 
	 */
	public ArrayList<ProductionStop> getAllStops() throws DbaException {
		PreparedStatement statement = null;
		String query = "SELECT * FROM getallstops ORDER BY stoptime DESC;";
		ResultSet result = null;
		ArrayList<ProductionStop> stopList = new ArrayList<>();
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()){
				while(result.next()){
					int id = result.getInt("id");
					long st = result.getLong("stoptime");
					int sl = result.getInt("stoplength");
					String sd = result.getString("stopdescription");
					int ttti = result.getInt("teamtimetableid");
					
					int teamId = result.getInt("id");
					long startTime = result.getLong("starttime");
					long endTime = result.getLong("endtime");
					String teamName = result.getString("teamname");
					int teamSize = result.getInt("workers");
					int department = result.getInt("department");
					Team team = new Team(teamId, startTime, endTime, teamName, teamSize, department);
					
					ProductionStop ps = new ProductionStop(id, st, sl, sd, ttti, team); // TODO select from teamtimetable ...
					stopList.add(ps);
				}
			}
			
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
			dbSinCon.closeConnection();
		}
		
		return stopList;
	}
	
	public ArrayList<Team> getTeamList(long epochDayStart) throws DbaException {
		ArrayList<Team> res = new ArrayList<>();
		PreparedStatement statement = null;
		String query = "DECLARE @daystart BIGINT = ?; DECLARE @dayend BIGINT = @daystart + 86400000; SELECT starttimestamp, endtimestamp, team.id, teamname, workers, department FROM teamtimetable JOIN team ON teamtimetable.teamid=team.id WHERE (starttimestamp BETWEEN @daystart AND @dayend) OR (endtimestamp BETWEEN @daystart AND @dayend);";
		ResultSet result = null;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			statement = con.prepareStatement(query);
			statement.setLong(1, epochDayStart);
			result = statement.executeQuery();
			if (result.isBeforeFirst()) {
				while (result.next()) {
					int teamId = result.getInt("id");
					long startTime = result.getLong("starttimestamp");
					long endTime = result.getLong("endtimestamp");
					String teamName = result.getString("teamname");
					int teamSize = result.getInt("workers");
					int department = result.getInt("department");
					
					res.add(new Team(teamId, startTime, endTime, teamName, teamSize, department));
				}
			}
		} catch (SQLException e) {
			throw new DbaException("Data kunne ikke findes", e);
		} finally {
			dbSinCon.closeConnection();
		}
		return res;
	}

	public ArrayList<DailyMessages> getDailyMessages() throws DbaException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
