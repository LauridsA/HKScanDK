package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Controller;
import dba.DBSingleConnection;
import dba.DatabaseAccess;
import model.WorkingTeam;

public class DailyScreenTests {
	public Controller ctr = null;
	public DatabaseAccess dba = null;
	public DBSingleConnection dbSinCon = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		dba = new DatabaseAccess();
		dbSinCon = new DBSingleConnection();
		ctr = new Controller(dbSinCon);
	}

	@After
	public void tearDown() throws Exception {
		ctr = null;
		dba = null;
		dbSinCon = null;
	}

	@Test
	public void SpeedTest() {
		fail("Not yet implemented");
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetWorkingTeam() {
		int team = 2;
		long starttimestamp = 1486008000000L;
		long endtimestamp = 1486036800000L;
		int id = 97;
		ctr.getCurrentWorkingTeam();
		
		assertEquals(WorkingTeam.getInstance().getTeamId(), team);
		assertEquals(WorkingTeam.getInstance().getStartTime(), starttimestamp);
		assertEquals(WorkingTeam.getInstance().getEndTime(), endtimestamp);
		assertEquals(WorkingTeam.getInstance().getTeamTimeTableId(), id);
	}

}
