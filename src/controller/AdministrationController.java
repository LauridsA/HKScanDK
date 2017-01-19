package controller;

import java.util.ArrayList;
import java.util.Date;

import dba.AdministrationDatabaseAccess;
import dba.DBSingleConnection;
import exceptions.DbaException;
import exceptions.PassThroughException;
import model.DailyMessages;
import model.ProductionStop;
import model.Team;

public class AdministrationController {
	private AdministrationDatabaseAccess dba;

	public AdministrationController(DBSingleConnection dbSinCon) {
		dba = new AdministrationDatabaseAccess(dbSinCon);	
	}
	
	public AdministrationController() {
		dba = new AdministrationDatabaseAccess();
	}
	
	/**
	 * Takes parameters from UI and inserts them into the dailymessages table in the database. <br>
	 * Creates a new DailyMessage to be displayed.
	 * @param message the string to be displayed.
	 * @param timestamp system Unix time stamp, generated at call.
	 * @param expire the expiration date for this message.
	 * @param showDate the time this message should be displayed from.
	 * @throws PassThroughException 

	 */
	public void createDailyMessage(String message, Long expire, Long showDate) throws PassThroughException{
		Date time = new Date();
		try {
			dba.createDailyMessage(message, time.getTime(), expire, showDate);
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Used to update an existing dailyMessage. <br>
	 * Takes parameters from UI and inserts them into the dailymessages table in the database at specified id.<br>
	 * If none of the fields are changed it simply re-inserts the old data.
	 * @param id the id from UI used to find the dailyMessage in the dailymessages table
	 * @param newMessage the new string to display
	 * @param newTimeStamp new Unix time stamp to use
	 * @param newExpire new expiration date
	 * @param newShowDate new time to display the message
	 * @throws PassThroughException 

	 */
	public void updateDailyMessage(int id, String newMessage, Long newTimeStamp, Long newExpire, Long newShowDate) throws PassThroughException{
		try {
			dba.updateDailyMessage(id, newMessage, newTimeStamp, newExpire, newShowDate);
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Used to delete a dailyMessage from the dailymessages table in database.
	 * Uses id to find the dailymessage to delete.
	 * @param id the id from UI used to find the dailyMessage in the dailymessages table.

	 */
	public void deleteDailyMessage(int id) throws PassThroughException {
		try {
			dba.deleteDailyMessage(id);
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Used to retrieve all DailyMessages from database.
	 * @return all DailyMessages from the dailymessages table as ArrayList
	 * @throws PassThroughException 

	 */
	public ArrayList<DailyMessages> getAllMessages() throws PassThroughException{
		try {
			return dba.getAllMessages();
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Used to create a production stop from administration.
	 * @param stopTime Unix time stamp at which this stop occurred.
	 * @param stopLength Length of the stop in minutes.
	 * @param stopDescription String displaying a description of the stop.
	 * @param teamTimeTableId ID of timetable working at the time of stop.
	 * @param team Team object used to display information in UI.
	 * @return the created productionStop along with a team object.
	 * @throws PassThroughException 

	 */
	public ProductionStop createStop(Long stopTime, int stopLength, String stopDescription, int teamTimeTableId, Team team) throws PassThroughException {
		try {
			return dba.createStop(stopTime, stopLength, stopDescription, teamTimeTableId, team);
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Used to update an existing ProductionStop. <br>
	 * Takes parameters from UI and inserts them into the productionstop table in the database at specified id.<br>
	 * If none of the fields are changed it simply re-inserts the old data.
	 * @param id the id from UI used to find the productionStop in the productionstop table
	 * @param newStopTime the new Unix time stamp
	 * @param newStopLength new int for stopLength
	 * @param newStopDescription new String to display
	 * @param newTeamTimeTableId new int for teamTimeTable
	 */
	public void updateStop(int id, Long newStopTime, int newStopLength, String newStopDescription, int newTeamTimeTableId) throws DbaException {
		dba.updateStop(id, newStopTime, newStopLength, newStopDescription, newTeamTimeTableId);
	}
	
	/**
	 * Used to delete a ProductionStop from the productionstop table in database.
	 * Uses id to find the productionStop to delete.
	 * @param id the id from UI used to find the productionStop in the productionstop table.
	 * @throws PassThroughException 
	 */
	public void deleteStop(int id) throws PassThroughException{
		try {
			dba.deleteStop(id);
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}
	
	/**
	 * Used to retrieve all ProductioStops from database.
	 * @return all DailyMessages from the productionstop table as ArrayList
	 * @throws PassThroughException 
	 */
	public ArrayList<ProductionStop> getAllStops() throws PassThroughException{
		try {
			return dba.getAllStops();
		} catch (DbaException e) {
			throw new PassThroughException(e.getMessage(), e);
		}
	}

}
