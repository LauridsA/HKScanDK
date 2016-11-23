package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

import dba.DatabaseAccess;
import model.FieldTypes;
import model.MyTypeHolder;

public class Controller {
		private DatabaseAccess dba = new DatabaseAccess();
	public MyTypeHolder getSpeed() {
		Date date = new Date();
		long time = date.getTime();
		dba.getSpeed(time-3600, time);
		Random rand = new Random();
		return new MyTypeHolder(rand.nextInt(1000));
	}
	public MyTypeHolder getAvgWeight() {
		//ResultSet res = dba.getAvgWeight();
		Random rand = new Random();
		return new MyTypeHolder(rand.nextInt(1000));
	}
	
	public int getAvgWeight(int fromTimeDate, int toTimeDate){
		ResultSet res = dba.getAvgWeight(fromTimeDate, toTimeDate);
		int total = 0;
		int average = 0;
		try {
			while (res.next()) {
				total += res.getInt("avgweight");
			}
			average = total / res.getFetchSize();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return average;
	}
	
	public MyTypeHolder getValue(FieldTypes fieldType){
		
		switch (fieldType) {
		case SPEED:
			return getSpeed();
		case AVGWEIGTH:
			return getAvgWeight();
		default:
			return new MyTypeHolder(1);
		}
	}

}
