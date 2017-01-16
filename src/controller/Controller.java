package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import dba.DBSingleConnection;
import dba.DatabaseAccess;
import exceptions.DbaException;
import exceptions.PassThroughException;
import model.FieldTypes;
import model.MyTypeHolder;
import model.ProductionStop;
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
	 * @throws PassThroughException 
	 */
	public MyTypeHolder getValue(FieldTypes fieldType) throws PassThroughException{
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
	 * @throws PassThroughException 
	 */
	public MyTypeHolder getSpeed() throws PassThroughException {
		try {
			return new MyTypeHolder(dba.getCurrentSpeed());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Retrieves the average weight from the DB and returns it as an int 
	 * @return the average weight on the current batch
	 * @throws PassThroughException 
	 */
	public MyTypeHolder getAvgWeight() throws PassThroughException {
		try {
			return new MyTypeHolder(dba.getAvgWeight());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}

	/**
	 * Retrieves the amount of chickens slaughtered by day team from DB
	 * @return amount of chickens slaughtered as an int by day team
	 * @throws PassThroughException 
	 */
	private MyTypeHolder getCurrentSlaughterAmountDay() throws PassThroughException {
		Date time = new Date();
		try {
			return new MyTypeHolder(dba.getSlaughterAmountDay(time.getTime()));
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}

	/**
	 * Retrieves the amount of chickens slaughtered by day night from DB
	 * @return amount of chickens slaughtered as an int by night team
	 * @throws PassThroughException 
	 */
	private MyTypeHolder getCurrentSlaughterAmountNight() throws PassThroughException {
		Date time = new Date();
		try {
			return new MyTypeHolder(dba.getSlaughterAmountNight(time.getTime()));
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}

	/**
	 * Updates the local machine to save who is the current working team (day or night, from team timetable)
	 * @return null
	 * @throws PassThroughException 
	 */
	@SuppressWarnings("static-access")
	public MyTypeHolder getCurrentWorkingTeam() throws PassThroughException {
		Date time = new Date();
		try {
			dba.getCurrentWorkingTeam(time.getTime());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
		return new MyTypeHolder(WorkingTeam.getInstance().getTeamTimeTableId());
	}

	/**
	 * the result of getCurrentTotalSlaughterAmount-getCurrentSlaughterAmount from the night team as an int
	 * @return the expected production for the day team as an int
	 * @throws PassThroughException 
	 */
	private MyTypeHolder dayExpected() throws PassThroughException {
		try {
			return new MyTypeHolder(dba.dayExpected(WorkingTeam.getInstance().getTeamId()));
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Retrieves the result of dayExpected / Speed from the database as a single query
	 * @return dayExpected / getSpeed as an Integer
	 * @throws PassThroughException 
	 */
	private MyTypeHolder expectedPerHour() throws PassThroughException {
		Date time = new Date();
		int eke;
		try {
			eke = dba.expectedPerHour(WorkingTeam.getInstance().getTeamId(),time.getTime());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
		if(eke <= 0) {
			eke = 0;
		}
		return new MyTypeHolder(eke);
	}

	/**
	 * Calculates the expected end time for the day team based on remaining amounts and speed.
	 * @return the expected end time for the day team as unix time
	 * @throws PassThroughException 
	 */
	private MyTypeHolder expectedFinish() throws PassThroughException {
		
		int totalTime = 0;
		Map<Integer, Integer> map;
		try {
			map = dba.expectedFinish(WorkingTeam.getInstance().getTeamId());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
		for (Map.Entry<Integer, Integer> entry : map.entrySet()){
			if (entry.getKey() == 1) {
				totalTime += entry.getValue() / 217;
			} else{
				totalTime += entry.getValue() / 83;
			}
		}
		Date date = new Date();
		Long newDate = date.getTime() + (totalTime *60000);
		return new MyTypeHolder(getFormattedTime(newDate, "HH:mm dd/MM/yy"));
		
	}
	
	/**
	 * retrieves the total slaughtered chickens for the working day as a single query
	 * @return the current total slaughtered chickens for the working day <b>so far</b> as an int
	 * @throws PassThroughException 
	 */
	public MyTypeHolder getTotalCurrentSlaughterAmount() throws PassThroughException {
		int myresult;
		try {
			myresult = dba.getTotalCurrentSlaughterAmount(WorkingTeam.getInstance().getTeamId());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * retrieves the currently non-expired production stop messages
	 * @return the object ProductionStop
	 * @throws PassThroughException 
	 */
	private MyTypeHolder getProductionStop() throws PassThroughException {
		ArrayList<ProductionStop> result;
		try {
			result = dba.getAllStops();
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
		return new MyTypeHolder(result);
	}
	
	/**
	 * Retrieves the total amount to be slaughtered for the working day.
	 * @return total amount to be slaughtered for the day as an integer.
	 * @throws PassThroughException 
	 */
	public MyTypeHolder getTotalSlaughterAmount() throws PassThroughException {
		try {
			return new MyTypeHolder(dba.totalSlaughterAmount(WorkingTeam.getInstance().getTeamId()));
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Retrieves the active daily messages objects.
	 * @return DailyMessages object in an array.
	 */
	private MyTypeHolder getDailyMessages() {
		// TODO About the same as getProductionstop. Boilerplate HO!
		return null;
	}
	
	/**
	 * Retrieves the amount of production stops for the working day, along with their stop time for the day team.
	 * @return amount of production stop and their respective stop time.
	 * @throws PassThroughException 
	 */
	private MyTypeHolder getNoStopDay() throws PassThroughException {
		Date time = new Date();
		int myresult;
		try {
			myresult = dba.getNoStopDay(time.getTime());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * Retrieves the amount of production stops for the working day, along with their stop time for the night team.
	 * @return amount of production stop and their respective stop time.
	 * @throws PassThroughException 
	 */
	private MyTypeHolder getNoStopNight() throws PassThroughException {
		Date time = new Date();
		int myresult;
		try {
			myresult = dba.getNoStopNight(time.getTime());
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * Retrieves all batches for the working day and evaluates whether any of them is organic.
	 * @return returns true, if any of the batches for the working day is organic.
	 * @throws PassThroughException 
	 */
	public MyTypeHolder getOrganic() throws PassThroughException {
		try {
			return new MyTypeHolder(dba.getOrganic(WorkingTeam.getInstance().getTeamId()));
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * @param type The type of the refresh rate to be retrieved (From {@link FieldTypes}).
	 * @return returns the refresh rate of the given type, 10 if it isn't found.
	 * @throws PassThroughException 
	 */
	public int getRefreshRate(FieldTypes type) throws PassThroughException {
		try {
			return dba.getRefreshRate(type);
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}

	/**
	 * Retrieves an integer representing the total amount of production stops.
	 * @return total amount of stops
	 * @throws PassThroughException 
	 */
	public int getTotalStops() throws PassThroughException {
		try {
			return dba.getTotalStops();
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Can be called in the case where we want time to be displayed (from Unix to good looking).
	 * @param Unix time stamp.
	 * @return Time in a good format.
	 */
	public String getFormattedTime(Long time) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		//dt.setTimeZone(new Ti);
		return dt.format(new Date(time));
		//dt.fot
	}
	
	/**
	 * Can be called in the case where we want time to be displayed (from Unix to good looking).
	 * @param Unix time stamp.
	 * @return Time in a good format.
	 */
	public String getFormattedTime(Long time, String format) {
		SimpleDateFormat dt = new SimpleDateFormat(format);
		return dt.format(new Date(time));
	}

}
