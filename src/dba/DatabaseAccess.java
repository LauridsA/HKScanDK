package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import model.FieldTypes;

public class DatabaseAccess {

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
			statement.setInt(1, fromTimeDate);
			statement.setInt(2, toTimeDate);
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
	public int getSpeed(int fromTimeStamp, int toTimeStamp) {
		PreparedStatement statement = null;
		String query = "SELECT value FROM speed WHERE (fromdate = ? AND todate = ?)";
		ResultSet result = null;
		int speed = 0;
		
		Connection con = null;
		try {
			con = DBConnection.getInstance().getDBcon();
			con.setAutoCommit(false);
			statement = con.prepareStatement(query);
			statement.setInt(1, fromTimeStamp);
			statement.setInt(2, toTimeStamp);
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

	public void getSpeed(long startTime, long endTime) {
		// TODO Auto-generated method stub
		
	}
	
	public int getRereshRate(FieldTypes type) {
		String sqlType;
		switch (type) {
		case SPEED:
			sqlType = "speed";
			break;
		case AVGWEIGHT:
			sqlType = "avgweight";
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
