package controller;

import java.util.Date;
import java.util.Map;

import dba.DBSingleConnection;
import dba.DatabaseAccess;
import exceptions.DbaException;
import model.MyTypeHolder;
import model.WorkingTeam;

public class ControllerTest {
	DBSingleConnection dbSinCon;
	DatabaseAccess dba;
	
	public ControllerTest(DBSingleConnection dbSinCon) {
		this.dbSinCon = dbSinCon;
		dba = new DatabaseAccess(dbSinCon);
	}

	/**
	 * Updates the local machine to save who is the current working team (day or night, from team timetable)
	 * @return null
	 * @throws DbaException 
	 */
	@SuppressWarnings("static-access")
	public MyTypeHolder getCurrentWorkingTeam(Long mytime) throws DbaException {
		dba.getCurrentWorkingTeam(mytime);
		return new MyTypeHolder(WorkingTeam.getInstance().getTeamTimeTableId());
	}
	
	public MyTypeHolder getSpeed() throws DbaException {
		return new MyTypeHolder(dba.getCurrentSpeed());
	}
	
	/**
	 * Calculates the expected end time for the day team based on remaining amounts and speed.
	 * @return the expected end time for the day team as unix time
	 * @throws DbaException 
	 */
	public MyTypeHolder expectedFinish() throws DbaException {
		int totalTime = 0;
		Map<Integer, Integer> map = dba.expectedFinish(WorkingTeam.getInstance().getTeamTimeTableId());
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
	 * Retrieves the amount of production stops for the working day, along with their stop time for the day team
	 * @return amount of production stop and their respective stop time
	 * @throws DbaException 
	 */
	public MyTypeHolder getNoStopDay(long now) throws DbaException {
		int myresult = dba.getNoStopDay(now);
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * Retrieves the result of dayExpected / Speed from the database as a single query
	 * @return number of chickens per hour (to finish on time)
	 * @throws DbaException 
	 */
	public MyTypeHolder expectedPerHour(int id) throws DbaException {
		Date time = new Date();
		return new MyTypeHolder(dba.expectedPerHour(id,time.getTime())); 
	}
	
	/**
	 * Retrieves the amount of chickens slaughtered by day team from DB
	 * @return amount of chickens slaughtered as an int by day team
	 * @throws DbaException 
	 */
	public MyTypeHolder getCurrentSlaughterAmountDay(long time) throws DbaException {
		return new MyTypeHolder(dba.getSlaughterAmountDay(time));
	}
	
	public MyTypeHolder getCurrentSlaughterAmountNight(long time) throws DbaException {
		return new MyTypeHolder(dba.getSlaughterAmountNight(time));
	}
	
	public MyTypeHolder getNoStopNight() throws DbaException {
		Date time = new Date();
		int myresult = dba.getNoStopNight(time.getTime());
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * retrieves the total slaughtered chickens for the working day as a single query
	 * @return the current total slaughtered chickens for the working day <b>so far</b> as an int
	 * @throws DbaException 
	 */
	public MyTypeHolder getTotalCurrentSlaughterAmount(int id) throws DbaException {
		int myresult = dba.getTotalCurrentSlaughterAmount(id);
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * the result of getCurrentTotalSlaughterAmount-getCurrentSlaughterAmount from the night team as an int
	 * @return the expected production for the day team as an int
	 * @throws DbaException 
	 */
	public MyTypeHolder dayExpected(int id) throws DbaException {
		return new MyTypeHolder(dba.dayExpected(id));
	}
	
	/**
	 * Retrieves the total amount to be slaughtered for the working day
	 * @return total amount to be slaughtered for the day as an int
	 * @throws DbaException 
	 */
	public MyTypeHolder getTotalSlaughterAmount(int id) throws DbaException {
		return new MyTypeHolder(dba.totalSlaughterAmount(id));
	}
	
	/**
	 * Retrieves the average weight from the DB and returns it as an int 
	 * @return the average weight on the current batch
	 * @throws DbaException 
	 */
	public MyTypeHolder getAvgWeight() throws DbaException {
		return new MyTypeHolder(dba.getAvgWeight());
	}
	
	/**
	 * Retrieves all batches for the working day and evaluates wether any of them is organic.
	 * @return returns true, if any of the batches for the working day is organic
	 * @throws DbaException 
	 */
	public MyTypeHolder getOrganic(int id) throws DbaException {
		return new MyTypeHolder(dba.getOrganic(id));
	}
	
	/**
	 * Retrieves the active daily messages objects
	 * @return DailyMessages object in an array
	 */
	public MyTypeHolder getDailyMessages() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * retrieves the currently non-expired production stop messages
	 * @return the object ProductionStop
	 */
	public MyTypeHolder getProductionStop() {
		// TODO Auto-generated method stub
		return null;
	}
}
