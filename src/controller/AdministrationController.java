package controller;

import java.util.ArrayList;
import java.util.Date;

import dba.AdministrationDatabaseAccess;
import dba.DBSingleConnection;
import model.DailyMessages;

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
	 */
	public void createDailyMessage(String message, Long expire, Long showDate) {
		Date time = new Date();
		dba.createDailyMessage(message, time.getTime(), expire, showDate);
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
	 */
	public void updateDailyMessage(int id, String newMessage, Long newTimeStamp, Long newExpire, Long newShowDate) {
		dba.updateDailyMessage(id, newMessage, newTimeStamp, newExpire, newShowDate);
	}
	
	/**
	 * Used to delete a dailyMessage from the dailymessages table in database.
	 * Uses id to find the dailymessage to delete.
	 * @param id the id from UI used to find the dailyMessage in the dailymessages table.
	 */
	public void deleteDailyMessage(int id) {
		dba.deleteDailyMessage(id);
	}
	
	/**
	 * Used to retrieve all DailyMessages from database.
	 * @return all DailyMessages from the dailymessages table as ArrayList
	 */
	public ArrayList<DailyMessages> getAllMessages() {
		return dba.getAllMessages();
	}

}
