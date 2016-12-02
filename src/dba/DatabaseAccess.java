package dba;

import java.awt.event.TextEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	/*
	public int getSlaughterAmount(long fromTimeStamp, long toTimeStamp) {
		
		PreparedStatement statement = null;
		String query = "SELECT value FROM slaughteramount WHERE satimestamp BETWEEN ? AND ?";
		ResultSet result = null;
		int amount = 0;
		
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(1, fromTimeStamp);
			statement.setLong(2, toTimeStamp);
			result = statement.executeQuery();
			con.commit();
			while (result.next()) {
				amount += result.getInt("value");
			}
			amount = amount / result.getFetchSize();
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
		return amount;
	}*/
		
	
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
	public int getSpeed(long fromTimeStamp, long toTimeStamp) {
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
				con.setAutoCommit(false);
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
	 * might be slightly generic, hopefully
	 * @param fromTimeStamp the start time of the desired speed
	 * @param toTimeStamp the end time of the desired speed
	 * @param field the enumerate value of the desired resultset value.
	 * @return ResultSet returns the resultset within the specified timeframe and of the specified enumerate value.
	 */
	/*public ResultSet getResultSetValue(long fromTimeStamp, long toTimeStamp, FieldTypes field){
		PreparedStatement statement = null;
		String query = "SELECT value FROM ? WHERE (fromdate = ? AND todate = ?)";
		ResultSet result = null;		
		Connection con = null;
		
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(2, fromTimeStamp);
			statement.setLong(3, toTimeStamp);
			statement.setString(1, field.toString());
			result = statement.executeQuery();
			con.commit();
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
		return result;
	}*/
	
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
	 * @param currentTime Current unix timestamp.
	 * @return TeamID of currently working team, retrieved from database at given timestamp.
	 * Sets the retrieved teamID for the UI class WorkingTeam at call.
	 */
	public void getWorkingTeam(long currentTime){
		
		PreparedStatement statement = null;
		String query = "DECLARE @time BIGINT = ?; SELECT team, id FROM teamtimetable WHERE (starttimestamp < @time AND @time < endtimestamp)";
		ResultSet result = null;
		int teamId = 0;
		int teamTimeTableId = 0;
		long startTime = 0;
		long endTime = 0;
		Connection con = null;
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(true);
			statement = con.prepareStatement(query);
			statement.setLong(1, currentTime);
			result = statement.executeQuery();
			result.next();
			teamId = result.getInt("team");
			
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
	
	/** TODO
	 * @param now
	 * @return
	 */
	public int getSlaughterAmountNight(long now) {
		PreparedStatement statement = null;
		String query = "DECLARE @now BIGINT = ?; SELECT SUM(value) AS currentamount FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamtimetable.id FROM teamtimetable JOIN team ON teamtimetable.team = team.id WHERE starttimestamp < @now AND teamname = 'nat' ORDER BY starttimestamp DESC)";
		ResultSet result = null;
		int amountNight = 0;
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
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
				DBConnection.getInstance().closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return amountNight;
	}
	
	// TODO needs to display 0 if the night team is working...
	public int getSlaughterAmountDay(long now) {
		PreparedStatement statement = null;
		String query = "DECLARE @now BIGINT = ?; SELECT SUM(value) AS currentamount FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamtimetable.id FROM teamtimetable JOIN team ON teamtimetable.team = team.id WHERE starttimestamp < @now AND teamname = 'dag' ORDER BY starttimestamp DESC)";
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
	 * @param teamId current team working.
	 * @return the sum of all rows of slaughteramount matching the given teamid.
	 */
	public int	getTotalExpected(int teamId) {
		PreparedStatement statement = null;
		String query = "SELECT SUM(value) AS totalamount FROM slaughteramount WHERE teamtimetableid = (SELECT TOP 1 teamnighttimetableid FROM batch WHERE teamdaytimetableid = ?)";
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
	
	
}
