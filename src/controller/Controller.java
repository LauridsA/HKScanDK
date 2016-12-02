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
			return getCurrentSlaughterAmountNight();
		case SLAUGTHERAMOUNTDAY:
			return getCurrentSlaughterAmountDay();
		case STOPNIGHT:
			return getNoStopNight();
		case STOPDAY:
			return getNoStopDay();
		case DAYEXPECTED:
			return dayExpected();
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
		case WORKINGTEAM:
			return getCurrentWorkingTeam();
		default:
			return new MyTypeHolder("fejl");
		}
	}
	
	/**
	 * Retrieves the speed from the DB and returns it as an int (average from the last hour)
	 * @return the speed as MyTypeHolder(int) for the last hour from DB
	 */
	public MyTypeHolder getSpeed() {
		return new MyTypeHolder(dba.getCurrentSpeed());
	}
	/**
	 * Retrieves the average weight from the DB and returns it as an int 
	 * @return the average weight on the current batch
	 */
	public MyTypeHolder getAvgWeight() {
		return new MyTypeHolder(dba.getAvgWeight());
	}

	/**
	 * Retrieves the amount of chickens slaughtered by day team from DB
	 * @return amount of chickens slaughtered as an int by day team
	 */
	private MyTypeHolder getCurrentSlaughterAmountDay() {
		Date time = new Date();
		return new MyTypeHolder(dba.getSlaughterAmountDay(time.getTime()));
	}

	/**
	 * Retrieves the amount of chickens slaughtered by day night from DB
	 * @return amount of chickens slaughtered as an int by night team
	 */
	private MyTypeHolder getCurrentSlaughterAmountNight() {
		Date time = new Date();
		return new MyTypeHolder(dba.getSlaughterAmountNight(time.getTime()));
	}

	/**
	 * Updates the local machine to save who is the current working team (day or night, from team timetable)
	 * @return null
	 */
	private MyTypeHolder getCurrentWorkingTeam() {
		Date time = new Date();
		dba.getCurrentWorkingTeam(1485403200000L);
		return null;
	}

	/**
	 * the result of getCurrentTotalSlaughterAmount-getCurrentSlaughterAmount from the night team as an int
	 * @return the expected production for the day team as an int
	 */
	private MyTypeHolder dayExpected() {
		// TODO Auto-generated method stub
		return new MyTypeHolder(10);
	}
	/**
	 * Retrieves the result of dayExpected / Speed from the database as a single query
	 * @return dayExpected / getSpeed as an Integer TODO
	 */
	private MyTypeHolder expectedPerHour() {
		MyTypeHolder dayExpected = dayExpected();
		int dayExpectedInt  = dayExpected.getInteger();
		int speed = getSpeed().getInteger();
		return new MyTypeHolder(dayExpectedInt / speed); 
	}

	/**
	 * Calculates the expected end time for the day team based on remaining amounts and speed.
	 * @return the expected end time for the day team as unix time TODO
	 */
	private MyTypeHolder expectedFinish() {
		MyTypeHolder slaughterAmount = getTotalCurrentSlaughterAmount();
		int slaughterAmountInt = slaughterAmount.getInteger();
		int speed = getSpeed().getInteger();
		return new MyTypeHolder(slaughterAmountInt  / speed);
	}
	/**
	 * retrieves the total slaughtered chickens for the working day as a single query
	 * @return the current total slaughtered chickens for the working day <b>so far</b> as an int
	 */
	private MyTypeHolder getTotalCurrentSlaughterAmount() {
		int myresult = dba.getTotalCurrentSlaughterAmount(WorkingTeam.getInstance().getTeamId());
		return new MyTypeHolder(myresult);
	}
	/**
	 * retrieves the currently non-expired production stop messages
	 * @return the object ProductionStop
	 */
	private MyTypeHolder getProductionStop() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Retrieves the total amount to be slaughtered for the working day
	 * @return total amount to be slaughtered for the day as an int
	 */
	private MyTypeHolder getTotalSlaughterAmount() {
		return new MyTypeHolder(dba.totalSlaughterAmount(WorkingTeam.getInstance().getTeamId()));
	}
	/**
	 * Retrieves the active daily messages objects
	 * @return DailyMessages object in an array
	 */
	private MyTypeHolder getDailyMessages() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Retrieves the amount of production stops for the working day, along with their stop time for the day team
	 * @return amount of production stop and their respective stop time
	 */
	private MyTypeHolder getNoStopDay() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Retrieves the amount of production stops for the working day, along with their stop time for the night team
	 * @return amount of production stop and their respective stop time
	 */
	private MyTypeHolder getNoStopNight() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Retrieves all batches for the working day and evaluates wether any of them is organic.
	 * @return returns true, if any of the batches for the working day is organic
	 */
	public MyTypeHolder getOrganic() {
		return new MyTypeHolder(dba.getOrganic(WorkingTeam.getInstance().getTeamId()));
	}
	
	/**
	 * @param type The type of the refresh rate to be retrieved (From {@link FieldTypes})
	 * @return returns the refresh rate of the given type, 10 if it isn't found
	 */
	public int getRefreshRate(FieldTypes type) {
		return dba.getRefreshRate(type);
	}

}
