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
		return new MyTypeHolder(dba.getSpeed(date.getTime()-3600000, date.getTime()));
	}
	public MyTypeHolder getAvgWeight() {
		//ResultSet res = dba.getAvgWeight();
		Date time = new Date();
		
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
		return dba.getAvgWeight(fromTimeDate, toTimeDate);
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
			return dayExpected();
		case TOTALEXPECTED:
			return totalExpected();
		case EXPECTEDFINISH:
			return expectedFinish();
		case EXPECTEDPERHOUR:
			return expectedPerHour();
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

	private MyTypeHolder dayExpected() {
		// TODO Auto-generated method stub
		return new MyTypeHolder(10);
	}
	/**
	 * Calls dayExpected and getSpeed, divides them and returns result.
	 * @return dayExpected / getSpeed as an Integer
	 */
	private MyTypeHolder expectedPerHour() {
		MyTypeHolder dayExpected = dayExpected();
		int dayExpectedInt  = dayExpected.getInteger();
		int speed = getSpeed().getInteger();
		return new MyTypeHolder(dayExpectedInt / speed); 
	}
	private MyTypeHolder totalExpected() {
		// TODO Auto-generated method stub
		return null;
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
	private MyTypeHolder getNoStop() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getSlaughterAmount() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return
	 */
	public MyTypeHolder getOrganic() {
		Date date = new Date();		
		return new MyTypeHolder(dba.getOrganic(date.getTime()));
	}
	public int getRefreshRate(FieldTypes type) {
		return dba.getRereshRate(type);
	}

}
