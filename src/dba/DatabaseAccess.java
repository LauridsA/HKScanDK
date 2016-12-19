package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import model.FieldTypes;
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
	 */
	public Boolean getOrganic(int teamid){
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
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
	 */
	public int getAvgWeight() {
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (result == null) {
			System.out.println("Database error: AvgWeight not found.");
		}
		return avgweight;
	}

	/**
	 * Retrieves the speed value between two given times.<br>
	 * Mostly an unused relic from an earlier iteration.
	 * @param fromTimeStamp the start time of the desired speed.
	 * @param toTimeStamp the end time of the desired speed.
	 * @return speed as an integer number.
	 */
	public int getAvgSpeed(long fromTimeStamp, long toTimeStamp) {
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		if (speed == 0) {
			System.out.println("Database error: Speed not found");
		}
		return speed;
	}
	
	/**
	 * Retrieves the most recent speed value from the database.
	 * @return current speed as integer.
	 */
	public int getCurrentSpeed() {
		PreparedStatement statement = null;
		String query = "SELECT TOP 1 value FROM speed ORDER BY stimestamp DESC";
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(false);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		if (speed == 0) {
			//This might be changed later, as the speed can be 0 without database errors
			System.out.println("Database error: Speed not found");
		}
		return speed;
	}
	
	/**
	 * Retrieves a refresh rate value for a given field.
	 * @param type Takes the parameter of enumerate FieldTypes, which can determine whih refreshrate to return.
	 * @return refreshrate Returns the refresh rate of the specified field as an integer.
	 */
	public int getRefreshRate(FieldTypes type) {
		String sqlType;
		switch (type) {
		case SPEED:
			sqlType = "speed";
			break;
		case AVGWEIGHT:
			sqlType = "avgweight";
			break;
		case ORGANIC:
			sqlType = "organic";
			break;
		case SLAUGTHERAMOUNTNIGHT:
			sqlType = "slaughteramount";
			break;
		case SLAUGTHERAMOUNTDAY:
			sqlType = "slaughteramount";
			break;
		case STOPNIGHT:
			sqlType = "productionstop";
			break;
		case STOPDAY:
			sqlType = "productionstop";
			break;
		case DAYEXPECTED:
			// TODO what goes here?
			sqlType = "";
			break;
		case EXPECTEDFINISH:
			sqlType = "";
			// TODO what goes here?
			break;
		case TOTALSLAUGTHERAMOUNT:
			sqlType = "slaughteramount";
			break;
		case PRODUCTIONSTOPS:
			sqlType = "productionstop";
			break;
		case DAILYMESSAGES:
			sqlType = "dailymessages";
			break;
		case WORKINGTEAM:
			sqlType = "teamid";
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
			con = DBConnection.getInstance().getDBcon();
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
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.getInstance().closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return res;
	}
	
	/**
	 * Retrieves and sets teamId, teamTimeTableId, startTime, and endTime for class WorkingTeam at given time stamp.
	 * @param time Unix time stamp.
	 */
	public void getCurrentWorkingTeam(long time){
		
		PreparedStatement statement = null;
		String query = "SELECT TOP 1 id, starttimestamp, endtimestamp, teamid FROM teamtimetable WHERE ? BETWEEN starttimestamp AND endtimestamp";
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
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		WorkingTeam.getInstance().setEverything(teamId, teamTimeTableId, startTime, endTime);
	}
	
	/** 
	 * Retrieves the current slaughtered amount of the night shift.
	 * @param now Specifies the current time in UNIX time stamp.
	 * @return integer of slaughtered night amount.
	 */
	public int getSlaughterAmountNight(long now) {
		PreparedStatement statement = null;
		String query = "DECLARE @now BIGINT = ?; SELECT SUM(value) AS currentamount FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamtimetable.id FROM teamtimetable JOIN team ON teamtimetable.teamid = team.id WHERE starttimestamp < @now AND teamname = 'nat' ORDER BY starttimestamp DESC)";
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
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return amountNight;
	}
	
	
	/**
	 * Retrieves amount of chickens slaughter by the day team.<br>
	 * Will display 0 if the night team is working.
	 * @param now Specifies the current time in UNIX time stamp.
	 * @return integer of slaughtered day amount.
	 */
	public int getSlaughterAmountDay(long now) {
		PreparedStatement statement = null;
		String query = "DECLARE @now BIGINT = ?; SELECT sum(value) AS currentamount FROM slaughteramount WHERE teamtimetableid = (SELECT teamtimetable.id  FROM teamtimetable JOIN team ON teamtimetable.teamid = team.id WHERE starttimestamp < @now AND teamname = 'dag' AND (endtimestamp + 14400000) > @now)";
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
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return amountDay;
	}
	
	/**
	 * Retrieves the total amount of chickens to be slaughtered by both the day and night team for the current workshift.
	 * @param teamId for specific team.
	 * @return the sum of all rows of slaughteramount matching the given teamtableid.
	 */
	public int	totalSlaughterAmount(int teamId) {
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
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return expectedAmount;
	}
	
	/**
	 * Retrieves the total amount of chickens slaughter by the current workshift.
	 * @param int teamId.
	 * @return the total slaughtered chickens so far for the working day.
	 */
	public int getTotalCurrentSlaughterAmount(int teamId){
		PreparedStatement statement = null;
		String query = "DECLARE @team INT = ?; SELECT sum(value) AS totalamount FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamnighttimetableid FROM batch WHERE teamnighttimetableid = @team OR teamdaytimetableid = @team) OR teamtimetableid = (SELECT TOP 1 teamdaytimetableid FROM batch WHERE teamnighttimetableid = @team OR teamdaytimetableid = @team);";
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return totalslaughteredcurrent;
	}
	
	/**
	 * Retrieves total number of production stops for the night team of the current workshift.
	 * @param teamId.
	 * @return noOfStops as an int for night team.
	 */
	public int getNoStopNight(long now){
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return noOfStops;
	}
	
	
	/**
	 * Retrieves total number of production stops for the night team of the current workshift.
	 * @param teamId.
	 * @return number of stops as int for day team.
	 */
	public int getNoStopDay(long now){
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return noOfStops;
	}
	
	/**
	 * Retrieves the amount of chickens the day team must slaughter to meet the goal for the day.
	 * @param teamId MUST BE DAY TEAM
	 * @return
	 */
	public int dayExpected(int teamId){
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
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				dbSinCon.closeConnection();
		} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				}
		}
		return expected;
	}

	/**
	 * Calculates and retrieves the amount of chickens expected to be slaughtered per hour.
	 * @param teamId MUST BE DAY TEAM.
	 * @return Integer value expected.
	 */
	public int expectedPerHour(int teamId, long now) {
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				dbSinCon.closeConnection();
		} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				}
		}
		return expected;
	}
	
	/**
	 * Calculates and retrieves the expected time at which the workshift will be done with the day's batches.
	 * @param teamId given id of team.
	 * @return A HashMap of k=Integer, v=Integer.<br>
	 * The key holds whether or not a result, held by value, is organic or not.
	 */
	public Map<Integer, Integer> expectedFinish(int teamId) {
		String query = "DECLARE @teamid INT = ?; SELECT beforebatch.organic, sum(ISNULL((beforebatch.value - afterbatch.value), beforebatch.value)) as result FROM (SELECT id, value, organic FROM batch WHERE teamnighttimetableid = @teamid OR teamdaytimetableid = @teamid) AS beforebatch  LEFT JOIN (SELECT slaughteramount.batchid, sum(slaughteramount.value) AS value FROM slaughteramount JOIN batch ON slaughteramount.batchid = batch.id WHERE batch.teamdaytimetableid = @teamid OR batch.teamnighttimetableid = @teamid GROUP BY slaughteramount.batchid) AS afterbatch  ON beforebatch.id = afterbatch.batchid GROUP BY beforebatch.organic;";
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
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				dbSinCon.closeConnection();
		} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				}
		}
		return total;
	}

	/**
	 * Retrieves the total amount of production stops from the database.
	 * @return Total amount of production stops as an integer.
	 */
	public int getTotalStops() {
	    String query = "SELECT count(id) as total FROM productionstop";
	    int total = 0;
	    try {
		Connection con = DBConnection.getInstance().getDBcon(); 
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		if(result.next()){
		    total = result.getInt("total");
		}
	    } catch (Exception e) {
		 e.printStackTrace();
	    } finally {
		DBConnection.getInstance().closeConnection();
	    }
	    return total;
	}
	
}
