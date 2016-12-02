package controller;

import java.util.Date;

import dba.DBSingleConnection;
import dba.DatabaseAccess;
import model.FieldTypes;
import model.MyTypeHolder;
import model.WorkingTeam;

public class Controller {
	private DatabaseAccess dba = new DatabaseAccess();
	
		
		
	public Controller(DBSingleConnection dbSinCon) {
		dba = new DatabaseAccess(dbSinCon);	
	}
	
	public Controller() {
		dba = new DatabaseAccess();
	}
	
	/**
	 * Primary method for retrieving values; Chooses the proper method.
	 * @param fieldType
	 * @return {@link MyTypeHolder}(Int/Bool/String)
	 */
	public MyTypeHolder getValue(FieldTypes fieldType){
		switch (fieldType) {
		case SPEED:
			return getSpeed();
		case AVGWEIGHT:
			return getAvgWeight();
		case ORGANIC: 
			return getOrganic();
		case SLAUGTHERAMOUNTNIGHT:
			return getSlaughterAmountNight();
		case SLAUGTHERAMOUNTDAY:
			return getSlaughterAmountDay();
		case STOPNIGHT:
			return getNoStopNight();
		case STOPDAY:
			return getNoStopDay();
		case DAYEXPECTED:
			return dayExpected();
		case TOTALEXPECTED:
			return totalExpected();
		case EXPECTEDFINISH:
			return expectedFinish();
		case EXPECTEDPERHOUR:
			return expectedPerHour();
		case TOTALSLAUGTHERAMOUNT:
			return getTotalSlaughterAmount();
		case PRODUCTIONSTOPS:
			return getProductionStop();
		case DAILYMESSAGES:
			return getDailyMessages();
		case CURRENTSLAUGHTERAMOUNT:
			return getTotalCurrentSlaughterAmount();
		case TEAMID:
			return getWorkingTeam();
		default:
			return new MyTypeHolder("fejl");
		}
	}
	
	/**
	 * Works
	 * @return the speed as MyTypeHolder(int) for the last hour
	 */
	public MyTypeHolder getSpeed() {
		Date date = new Date();
		return new MyTypeHolder(dba.getSpeed(date.getTime()-3600000, date.getTime()));
	}
	/**
	 * @return
	 */
	public MyTypeHolder getAvgWeight() {
		return new MyTypeHolder(dba.getAvgWeight());
	}

	private MyTypeHolder getSlaughterAmountDay() {
		Date time = new Date();
		return new MyTypeHolder(dba.getSlaughterAmountDay(time.getTime()));
	}

	private MyTypeHolder getSlaughterAmountNight() {
		Date time = new Date();
		return new MyTypeHolder(dba.getSlaughterAmountNight(time.getTime()));
	}

	private MyTypeHolder getWorkingTeam() {
		Date time = new Date();
		WorkingTeam.getInstance().setTeamId(dba.getWorkingTeam(time.getTime()));
		return new MyTypeHolder(WorkingTeam.getInstance().getTeamId());
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
	
	// TODO probably don't use teamId...
	private MyTypeHolder totalExpected() {
		// TODO Auto-generated method stub
		return new MyTypeHolder(dba.getTotalExpected(WorkingTeam.getInstance().getTeamId()));
	}
	private MyTypeHolder expectedFinish() {
		MyTypeHolder slaughterAmount = getTotalCurrentSlaughterAmount();
		int slaughterAmountInt = slaughterAmount.getInteger();
		int speed = getSpeed().getInteger();
		return new MyTypeHolder(slaughterAmountInt  / speed);
	}
	private MyTypeHolder getTotalCurrentSlaughterAmount() {
		
		return null;
	}
	private MyTypeHolder getProductionStop() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getTotalSlaughterAmount() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getDailyMessages() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getNoStopDay() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getNoStopNight() {
		// TODO Auto-generated method stub
		return null;
	}
	private MyTypeHolder getSlaughterAmount() {
		Date date = new Date();
		return new MyTypeHolder(dba.getSlaughterAmount(date.getTime()-3600000, date.getTime()));
	}
	/**
	 * @return
	 */
	public MyTypeHolder getOrganic() {
		return new MyTypeHolder(dba.getOrganic(WorkingTeam.getInstance().getTeamId()));
	}
	
	public int getRefreshRate(FieldTypes type) {
		return dba.getRefreshRate(type);
	}

}
