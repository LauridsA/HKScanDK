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
	
	/**
	 * calculates average from a resultset from DB
	 * @param fromTimeDate
	 * @param toTimeDate
	 * @return average weight within specified time frame
	 */
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
		case AVGWEIGHT:
			return getAvgWeight();
		case ORGANIC: 
			return getOrganic();
		case SLAUGTHERAMOUNTNIGHT:
			return getSlaughterAmount();
		case SLAUGTHERAMOUNTDAY:
			return getSlaughterAmount();
		case STOPNIGHT:
			return getNoStop();
		case STOPDAY:
			return getNoStop();
		case DAYEXPECTED:
			return expected();
		case TOTALEXPECTED:
			return expected();
		case EXPECTEDFINISH:
			return expectedFinish();
		case TOTALSLAUGTHERAMOUNT:
			return totalSlaughterAmount();
		case PRODUCTIONSTOPS:
			return getProductionStop();
		case DAILYMESSAGES:
			return getDailyMessages();
		default:
			return new MyTypeHolder("fejl");
		}
	}

	private MyTypeHolder expectedFinish() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder totalSlaughterAmount() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getProductionStop() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getDailyMessages() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder expected() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getNoStop() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getSlaughterAmount() {
		// TODO Auto-generated method stub
		return null;
	}
	public MyTypeHolder getOrganic() {
		Date date = new Date();
		ResultSet result;
		Boolean organic;
		date.getTime();
		long time = date.getTime();
		result = dba.getOrganic(time-3600, time);
		try {
			while(result.next()){
				organic = result.getBoolean("organic");
				if(organic){
					return new MyTypeHolder(true);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return new MyTypeHolder(false);
	}
	public int getRefreshRate(FieldTypes type) {
		return dba.getRereshRate(type);
	}

}
