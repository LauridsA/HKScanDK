package controller;

import java.util.Date;
import java.util.Map;

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
	@SuppressWarnings("static-access")
	public MyTypeHolder getCurrentWorkingTeam() {
		Date time = new Date();
		dba.getCurrentWorkingTeam(time.getTime());
		return new MyTypeHolder(WorkingTeam.getInstance().getTeamTimeTableId());
	}

	/**
	 * the result of getCurrentTotalSlaughterAmount-getCurrentSlaughterAmount from the night team as an int
	 * @return the expected production for the day team as an int
	 */
	private MyTypeHolder dayExpected() {
		return new MyTypeHolder(dba.dayExpected(WorkingTeam.getInstance().getTeamId()));
	}
	/**
	 * Retrieves the result of dayExpected / Speed from the database as a single query
	 * @return dayExpected / getSpeed as an Integer
	 */
	private MyTypeHolder expectedPerHour() {
		return new MyTypeHolder(dba.expectedPerHour(WorkingTeam.getInstance().getTeamId())); 
	}

	/**
	 * Calculates the expected end time for the day team based on remaining amounts and speed.
	 * @return the expected end time for the day team as unix time
	 */
	private MyTypeHolder expectedFinish() {
		Date date = new Date();
		int totalTime = 0;
		Map<Integer, Integer> map = dba.expectedFinish(WorkingTeam.getInstance().getTeamId());
		for (Map.Entry<Integer, Integer> entry : map.entrySet()){
			if (entry.getKey() == 1) {
				totalTime += entry.getValue() / 217;
			} else{
				totalTime += entry.getValue() / 83;
			}
		}
		
		return new MyTypeHolder(totalTime);
		
	}
	/**
	 * retrieves the total slaughtered chickens for the working day as a single query
	 * @return the current total slaughtered chickens for the working day <b>so far</b> as an int
	 */
	public MyTypeHolder getTotalCurrentSlaughterAmount() {
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
	public MyTypeHolder getTotalSlaughterAmount() {
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
		int myresult = dba.getNoStopDay(WorkingTeam.getInstance().getTeamId());
		return new MyTypeHolder(myresult);
	}
	/**
	 * Retrieves the amount of production stops for the working day, along with their stop time for the night team
	 * @return amount of production stop and their respective stop time
	 */
	
	private MyTypeHolder getNoStopNight() {
		int myresult = dba.getNoStopNight(WorkingTeam.getInstance().getTeamId());
		return new MyTypeHolder(myresult);
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
