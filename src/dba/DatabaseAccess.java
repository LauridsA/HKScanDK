package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import model.FieldTypes;

public class DatabaseAccess {
	
	
	public Boolean getOrganic(long now){
		PreparedStatement statement = null;
		String query = "DECLARE @time BIGINT = ?; IF EXISTS (SELECT id FROM teamtimetable WHERE (starttimestamp < @time AND @time < endtimestamp)) SELECT timetablenight.id as nightid, timetableday.id as dayid, organic, timetablenight.starttimestamp as nightstarttimestamp, timetablenight.endtimestamp as nightendtimestamp, timetableday.starttimestamp as daystarttimestamp, timetableday.endtimestamp as dayendtimestamp FROM batch JOIN teamtimetable AS timetableday ON timetableday.id = batch.teamdaytimetableid JOIN teamtimetable AS timetablenight ON timetablenight.id = batch.teamnighttimetableid WHERE (timetableday.starttimestamp < @time AND @time < timetableday.endtimestamp) OR (timetablenight.starttimestamp < @time AND @time < timetablenight.endtimestamp) ELSE SELECT timetablenight.id as nightid, timetableday.id as dayid, organic, timetablenight.starttimestamp as nightstarttimestamp, timetablenight.endtimestamp as nightendtimestamp, timetableday.starttimestamp as daystarttimestamp, timetableday.endtimestamp as dayendtimestamp FROM batch JOIN teamtimetable AS timetableday ON timetableday.id = batch.teamdaytimetableid JOIN teamtimetable AS timetablenight ON timetablenight.id = batch.teamnighttimetableid WHERE timetableday.starttimestamp > @time OR timetablenight.starttimestamp > @time";
		ResultSet result = null;
		Connection con = null;
		Boolean organic = false;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(1, now);
			result = statement.executeQuery();
			con.commit();
			while (result.next()) {
				System.out.print(result.getInt("nightid") + " " + result.getLong("nightstarttimestamp") + " " + result.getLong("endtimestamp") + " night: " + result.getLong("timetableday.endtimestamp") + " " + result.getLong("timetableday.endtimestamp") + " now: " + now);
				System.out.print(result.getInt("dayid") + " " + result.getLong("daystarttimestamp") + " " + result.getLong("endtimestamp") + " night: " + result.getLong("timetableday.endtimestamp") + " " + result.getLong("timetableday.endtimestamp") + " now: " + now);
				if(result.getBoolean("organic")){
					organic = true;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return organic;
	}

	/**
	 * @param fromTimeDate the start time of the desired average weight
	 * @param toTimeDate the end time of the desired average weight
	 * @return the result as a multi dimensional array (ResultSet)
	 */
	public ResultSet getAvgWeight(int fromTimeDate, int toTimeDate) {
		PreparedStatement statement = null;
		String query = "SELECT avgweight FROM batch WHERE (fromdate = ? AND todate = ?)";
		ResultSet result = null;
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(1, fromTimeDate);
			statement.setLong(2, toTimeDate);
			result = statement.executeQuery();
			con.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (result == null) {
			System.out.println("Database error: nothing found.");
		}
		return result;
	}

	/**
	 * @param fromTimeStamp the start time of the desired speed
	 * @param toTimeStamp the end time of the desired speed
	 * @return speed as an integer number
	 */
	public int getSpeed(long fromTimeStamp, long toTimeStamp) {
		PreparedStatement statement = null;
		String query = "SELECT value FROM speed WHERE (fromdate = ? AND todate = ?)";
		ResultSet result = null;
		int speed = 0;
		
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
				speed = result.getInt("value");
				// TODO average speed???
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				con.setAutoCommit(false);
				DBConnection.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (speed == 0) {
			System.out.println("Database error: Nothing found");
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
	public ResultSet getResultSetValue(long fromTimeStamp, long toTimeStamp, FieldTypes field){
		PreparedStatement statement = null;
		String query = "SELECT value FROM ? WHERE (fromdate = ? AND todate = ?)";
		ResultSet result = null;		
		Connection con = null;
		
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(2, fromTimeStamp);
			statement.setLong(3, toTimeStamp);
			statement.setString(1, field.toString());
			result = statement.executeQuery();
			con.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * @param type Takes the parameter of enumerate FieldTypes, which can determine whih refreshrate to return
	 * @return refreshrate Returns the refresh rate of the specified field as an integer
	 */
	public int getRereshRate(FieldTypes type) {
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
		case TOTALEXPECTED:
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
		default:
			// TODO throw exception
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
			result.next();
			res = result.getInt("refreshrate");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (res == 0) {
			res = 10;
		}
		System.out.println(res);
		return res;
	}

	public long getTeamNightStart() {
		PreparedStatement statement = null;
		String query = "SELECT starttimestamp FROM team WHERE teamname = 'nat'";
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
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return startTime;
	}

}
