package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseAccess {

	public ResultSet getAvgWeight(int fromTimeDate, int toTimeDate) {
		PreparedStatement statement = null;
		String query = "SELECT (avgweight) FROM batch WHERE (fromdate = ? AND todate = ?)";
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
				con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (result == null) {
			System.out.println("Database error: nothing found.");
		}
		return result;
	}

	public int getSpeed(int fromTimeStamp, int toTimeStamp) throws SQLException {
		PreparedStatement statement = null;
		String query = "SELECT (value) FROM speed WHERE (fromdate = ? AND todate = ?)";
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
			con.setAutoCommit(false);
			con.close();
		}
		if (speed == 0) {
			System.out.println("Database error: Nothing found");
		}
		return speed;
	}

	public void getSpeed(long startTime, long endTime) {
		// TODO Auto-generated method stub
		
	}

}
