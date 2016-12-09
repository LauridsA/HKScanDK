package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * Can be called in the case where we want time to be displayed (from Unix to good looking)
	 * @param Unix Timestamp
	 * @return Time in a good format
	 */
	private String getTime(Long time) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		//dt.setTimeZone(new Ti);
		return dt.format(new Date(time));
		//dt.fot
	}
	
	/**
	 * 
	 * @param fromTimeStamp start of shift
	 * @param toTimeStamp current time
	 * @return
	 */
	
//	public int getSlaughterAmount(long fromTimeStamp, long toTimeStamp) {
//		
//		PreparedStatement statement = null;
//		String query = "SELECT value FROM slaughteramount WHERE satimestamp BETWEEN ? AND ?";
//		ResultSet result = null;
//		int amount = 0;
//		
//		Connection con = null;
//		try {
//			con = DBConnection.getInstance().getDBcon();
//			con.setAutoCommit(false);
//			statement = con.prepareStatement(query);
//			statement.setLong(1, fromTimeStamp);
//			statement.setLong(2, toTimeStamp);
//			result = statement.executeQuery();
//			con.commit();
//			while (result.next()) {
//				amount += result.getInt("value");
//			}
//			amount = amount / result.getFetchSize();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.setAutoCommit(false);
//				dbSinCon.closeConnection();
//			} catch (SQLException e) {
//				System.out.println(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		return amount;
//	}
		
	
	/**
	 * @param WorkingTeam id of the current working team
	 * @return True, if any of the days' batches are organic
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
	 * 
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
	 * @param fromTimeStamp the start time of the desired speed
	 * @param toTimeStamp the end time of the desired speed
	 * @return speed as an integer number
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
	 * @param type Takes the parameter of enumerate FieldTypes, which can determine whih refreshrate to return
	 * @return refreshrate Returns the refresh rate of the specified field as an integer
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
	 * @return 
	 */
	/*
	public long getTeamNightStart() {
		PreparedStatement statement = null;
		String query = "SELECT starttimestamp FROM teamtimetable WHERE team = '2'";
		ResultSet result = null;
		long startTime = 0;
		
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(true);
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			result.next();
			startTime = result.getLong("starttimestamp");
			
		} catch (Exception e) {
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
		return startTime;
	}*/
	
	/**
	 * Retrieves and sets teamId, teamTimeTableId, startTime, and endTime for class WorkingTeam at given timestamp
	 * @param time unix timestamp.
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
	 * @param now Specifices the current time in UNIX Timestamp
	 * @return int of slaughtered night amount
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
	 * Will display 0 if the night team is working...
	 * @param now Specifices the current time in UNIX Timestamp 
	 * @return int of slaughtered day amount
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
	 * @param teamId for specific team
	 * @return the sum of all rows of slaughteramount matching the given teamtableid.
	 */
	public int	totalSlaughterAmount(int teamId) {
		PreparedStatement statement = null;
		String query = "DECLARE @team int = ?; SELECT SUM(value) AS totalamount FROM batch WHERE teamnighttimetableid = @team OR teamdaytimetableid = @team;";
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
	 * @param teamId
	 * @return int: the total slaughtered chickens so far for the working day
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
	 * @param teamId
	 * @return noOfStops as an int for night team
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
	 * @param teamId
	 * @return number of stops as int for day team
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
	 * @param teamId MUST BE DAY TEAM
	 * @return
	 */
	public int expectedPerHour(int teamId) {
		String query = "DECLARE @team INT = ?; DECLARE @NightTeamAmount INT = (SELECT SUM(value) AS nightamount FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamnighttimetableid FROM teamtimetable JOIN batch ON teamtimetable.id = batch.teamdaytimetableid WHERE teamdaytimetableid = @team)); DECLARE @speed INT = (SELECT TOP 1 value FROM speed ORDER BY stimestamp DESC); SELECT (sum(value) - @NightTeamAmount)/@speed AS result FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamnighttimetableid FROM batch WHERE teamnighttimetableid = @team OR teamdaytimetableid = @team) OR teamtimetableid = (SELECT TOP 1 teamdaytimetableid FROM batch WHERE teamnighttimetableid = @team OR teamdaytimetableid = @team);";
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
	
	public Map<Integer, Integer> expectedFinish(int teamId) {
		String query = "DECLARE @teamid INT = ?; SELECT beforebatch.organic, sum(ISNULL((beforebatch.value - afterbatch.value), beforebatch.value)) as result FROM (SELECT id, value, organic FROM batch WHERE teamnighttimetableid = @teamid OR teamdaytimetableid = @teamid) AS beforebatch  LEFT JOIN (SELECT slaughteramount.batchid, sum(slaughteramount.value) AS value FROM slaughteramount JOIN batch ON slaughteramount.batchid = batch.id WHERE batch.teamdaytimetableid = @teamid OR batch.teamnighttimetableid = @teamid GROUP BY slaughteramount.batchid) AS afterbatch  ON beforebatch.id = afterbatch.batchid GROUP BY beforebatch.organic;";
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection con = null;
		Map<Integer, Integer> total = new HashMap<Integer, Integer>();
		
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
