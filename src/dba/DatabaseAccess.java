package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import model.FieldTypes;

public class DatabaseAccess {
	private DBSingleConnection dbSinCon;
	
	public DatabaseAccess(DBSingleConnection dbSinCon) {
		this.dbSinCon = dbSinCon;
	}
	
	public DatabaseAccess() {
		// TODO Auto-generated constructor stub
	}


	private String getTime(Long time) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		//dt.setTimeZone(new Ti);
		return dt.format(new Date(time));
		//dt.fot

	}
	
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
			e.getStackTrace();
		} finally {
			try {
				con.setAutoCommit(false);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.getStackTrace();
			}
		}
		if (amount == 0) {
			System.out.println("Database error: Nothing found");
		}
		return amount;
	}
		
	
	public Boolean getOrganic(long now){
		PreparedStatement statement = null;
		String query = "DECLARE @time BIGINT = ?; IF EXISTS (SELECT id FROM teamtimetable WHERE (starttimestamp < @time AND @time < endtimestamp)) SELECT timetablenight.id as nightid, timetableday.id as dayid, organic, timetablenight.starttimestamp as nightstarttimestamp, timetablenight.endtimestamp as nightendtimestamp, timetableday.starttimestamp as daystarttimestamp, timetableday.endtimestamp as dayendtimestamp FROM batch JOIN teamtimetable AS timetableday ON timetableday.id = batch.teamdaytimetableid JOIN teamtimetable AS timetablenight ON timetablenight.id = batch.teamnighttimetableid WHERE (timetableday.starttimestamp < @time AND @time < timetableday.endtimestamp) OR (timetablenight.starttimestamp < @time AND @time < timetablenight.endtimestamp) ELSE SELECT timetablenight.id as nightid, timetableday.id as dayid, organic, timetablenight.starttimestamp as nightstarttimestamp, timetablenight.endtimestamp as nightendtimestamp, timetableday.starttimestamp as daystarttimestamp, timetableday.endtimestamp as dayendtimestamp FROM batch JOIN teamtimetable AS timetableday ON timetableday.id = batch.teamdaytimetableid JOIN teamtimetable AS timetablenight ON timetablenight.id = batch.teamnighttimetableid WHERE timetableday.starttimestamp > @time OR timetablenight.starttimestamp > @time";
		ResultSet result = null;
		Connection con = null;
		Boolean organic = false;
		
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(1, now);
			result = statement.executeQuery();
			con.commit();
			
			
			while (result.next()) {
				
				System.out.println(getTime(result.getLong("daystarttimestamp")));
				//System.out.print(result.getBoolean("organic") + "\tnigth " +  result.getInt("nightid") + " " + result.getLong("nightstarttimestamp") + " " + result.getLong("nightendtimestamp"));
				//System.out.print(" day " + result.getInt("dayid") + " " + result.getLong("daystarttimestamp") + " " + result.getLong("dayendtimestamp"));
				//System.out.println(" now: " + now);
				//new Date(result.getLong("daystarttimestamp"));
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
				dbSinCon.closeConnection();
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
	public int getAvgWeight(long now) {
		PreparedStatement statement = null;
		String query = "DECLARE @time BIGINT = ?; IF EXISTS (SELECT id FROM teamtimetable WHERE (starttimestamp < @time AND @time < endtimestamp)) SELECT timetablenight.id as nightid, timetableday.id as dayid, avgweight, timetablenight.starttimestamp as nightstarttimestamp, timetablenight.endtimestamp as nightendtimestamp, timetableday.starttimestamp as daystarttimestamp, timetableday.endtimestamp as dayendtimestamp FROM batch JOIN teamtimetable AS timetableday ON timetableday.id = batch.teamdaytimetableid JOIN teamtimetable AS timetablenight ON timetablenight.id = batch.teamnighttimetableid WHERE (timetableday.starttimestamp < @time AND @time < timetableday.endtimestamp) OR (timetablenight.starttimestamp < @time AND @time < timetablenight.endtimestamp) ELSE SELECT timetablenight.id as nightid, timetableday.id as dayid, avgweight, timetablenight.starttimestamp as nightstarttimestamp, timetablenight.endtimestamp as nightendtimestamp, timetableday.starttimestamp as daystarttimestamp, timetableday.endtimestamp as dayendtimestamp FROM batch JOIN teamtimetable AS timetableday ON timetableday.id = batch.teamdaytimetableid JOIN teamtimetable AS timetablenight ON timetablenight.id = batch.teamnighttimetableid WHERE timetableday.starttimestamp > @time OR timetablenight.starttimestamp > @time";
		ResultSet result = null;
		Connection con = null;
		int avgweight = 0;
		
		try {
			con = dbSinCon.getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setLong(1, now);
			result = statement.executeQuery();
			con.commit();
			avgweight = result.getInt("avgweight");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.getDBcon();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (result == null) {
			System.out.println("Database error: nothing found.");
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
			e.getStackTrace();
		} finally {
			try {
				con.setAutoCommit(false);
				dbSinCon.closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.getStackTrace();
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
		} finally {
			try {
				con.setAutoCommit(true);
				dbSinCon.closeConnection();
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
				DBConnection.getInstance().closeConnection();
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
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.getInstance().closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return startTime;
	}
	
	public int getCurrentTeamId(long currentTime){
		
		PreparedStatement statement = null;
		
		
		String query = "DECLARE @time BIGINT = 1480479800000; SELECT team, id FROM teamtimetable WHERE (starttimestamp < @time AND @time < endtimestamp) ";
		ResultSet result = null;
		int teamId = 0;
		
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(true);
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			result.next();
			teamId = result.getInt("starttimestamp");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				con.setAutoCommit(true);
				DBConnection.getInstance().closeConnection();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return teamId;
	}
	}	
		return 0;
		
	}
	

}
