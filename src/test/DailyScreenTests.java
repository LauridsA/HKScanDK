package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Controller;
import controller.ControllerTest;
import dba.DBSingleConnection;
import dba.DatabaseAccess;
import model.WorkingTeam;

public class DailyScreenTests {
	public ControllerTest ctrt = null;
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
		ctrt = new ControllerTest(dbSinCon);
	}

	@After
	public void tearDown() throws Exception {
		ctrt = null;
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
		ctrt.getCurrentWorkingTeam(1486036700000L);
		
		assertEquals(WorkingTeam.getInstance().getTeamId(), team);
		assertEquals(WorkingTeam.getInstance().getStartTime(), starttimestamp);
		assertEquals(WorkingTeam.getInstance().getEndTime(), endtimestamp);
		assertEquals(WorkingTeam.getInstance().getTeamTimeTableId(), id);
	}

}
