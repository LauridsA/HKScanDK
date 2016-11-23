package controller;

import java.sql.ResultSet;

import dba.DatabaseAccess;

public class Controller {
		private DatabaseAccess dba = new DatabaseAccess();
	public int getSpeed(int i, int j) {
		dba.getSpeed(I, j);
		
		return 0;
	}
	public int getAvgWeight(int before, int now) {
		ResultSet res = dba.getAvgWeight();
		return 0;
	}

}
