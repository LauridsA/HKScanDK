package controller;

import java.util.Date;
import java.util.Map;

import dba.DBSingleConnection;
import dba.DatabaseAccess;
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
	 */
	@SuppressWarnings("static-access")
	public MyTypeHolder getCurrentWorkingTeam(Long mytime) {
		dba.getCurrentWorkingTeam(mytime);
		return new MyTypeHolder(WorkingTeam.getInstance().getTeamTimeTableId());
	}
	
	public MyTypeHolder getSpeed() {
		return new MyTypeHolder(dba.getCurrentSpeed());
	}
	
	/**
	 * Calculates the expected end time for the day team based on remaining amounts and speed.
	 * @return the expected end time for the day team as unix time
	 */
	public MyTypeHolder expectedFinish() {
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
	 * Retrieves the amount of production stops for the working day, along with their stop time for the day team
	 * @return amount of production stop and their respective stop time
	 */
	public MyTypeHolder getNoStopDay(long now) {
		int myresult = dba.getNoStopDay(now);
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * Retrieves the result of dayExpected / Speed from the database as a single query
	 * @return number of chickens per hour (to finish on time)
	 */
	public MyTypeHolder expectedPerHour() {
		return new MyTypeHolder(dba.expectedPerHour(WorkingTeam.getInstance().getTeamId())); 
	}
	
	/**
	 * Retrieves the amount of chickens slaughtered by day team from DB
	 * @return amount of chickens slaughtered as an int by day team
	 */
	public MyTypeHolder getCurrentSlaughterAmountDay() {
		Date time = new Date();
		return new MyTypeHolder(dba.getSlaughterAmountDay(time.getTime()));
	}
	
	/**
	 * retrieves the total slaughtered chickens for the working day as a single query
	 * @return the current total slaughtered chickens for the working day <b>so far</b> as an int
	 */
	public MyTypeHolder getTotalCurrentSlaughterAmount(int id) {
		int myresult = dba.getTotalCurrentSlaughterAmount(id);
		return new MyTypeHolder(myresult);
	}
	
	/**
	 * the result of getCurrentTotalSlaughterAmount-getCurrentSlaughterAmount from the night team as an int
	 * @return the expected production for the day team as an int
	 */
	public MyTypeHolder dayExpected(int id) {
		return new MyTypeHolder(dba.dayExpected(id));
	}
	
	/**
	 * Retrieves the total amount to be slaughtered for the working day
	 * @return total amount to be slaughtered for the day as an int
	 */
	public MyTypeHolder getTotalSlaughterAmount(int id) {
		return new MyTypeHolder(dba.totalSlaughterAmount(id));
	}
}
