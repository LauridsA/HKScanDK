package dba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseAccess {

	public ResultSet getAvgWeight(int fromTimeDate, int toTimeDate) {
		PreparedStatement statement = null;
		String query = "SELECT (avgweight) FROM batch WHERE ";
		ResultSet result;
		
		Connection con = null;
		return result;
	}

	public void getSpeed(int i, int j) {
		// TODO Auto-generated method stub
		
	}

}
