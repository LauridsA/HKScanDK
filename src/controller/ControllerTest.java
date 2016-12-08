package controller;

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
}
