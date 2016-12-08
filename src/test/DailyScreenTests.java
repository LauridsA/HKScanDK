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
import model.MyTypeHolder;
import model.WorkingTeam;

public class DailyScreenTests {
	public ControllerTest ctrt = null;
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
		ctrt = new ControllerTest(dbSinCon);
		ctrt.getCurrentWorkingTeam(1480305900000L);
	}

	@After
	public void tearDown() throws Exception {
		ctrt = null;
		dba = null;
		dbSinCon = null;
		ctr.getCurrentWorkingTeam();
	}

	/**
	 * Test to try and get the latest speed entry from DB. Should pass.
	 */
	@Test
	public void SpeedTest() {
		MyTypeHolder speed = ctrt.getSpeed();
		int speedint = speed.getInteger();
		assertEquals(speedint, 13000);
	}
	
	/**
	 * Test to try and get the latest speed entry from DB. Should fail.
	 */
	@Test
	public void SpeedTestFail() {
		MyTypeHolder speed = ctrt.getSpeed();
		int speedint = speed.getInteger();
		assertEquals(speedint, 13001);
	}
	
	/**
	 * Test the working team method. Should pass.
	 */
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
	
	@Test
	public void testExpectedFinish(){
		
		MyTypeHolder testRes = ctrt.expectedFinish();
		int testResInt = testRes.getInteger();
		System.out.println(testResInt);
		assertEquals(1481198400000L, (long)testResInt);
	}
	
	@Test
	public void testExpectedPerHour(){
		
		MyTypeHolder testRes = ctrt.expectedPerHour();
		int testResInt = testRes.getInteger();
		System.out.println(testResInt);
		assertEquals(1481198400000L, testResInt);
	}
	
	@Test
	public void testDayExpected(){
		MyTypeHolder testRes = ctrt.dayExpected();
		int testResInt = testRes.getInteger();
		System.out.println(testResInt);
		assertEquals(848, testResInt);
	}
	
	@Test
	public void testGetTotalSlaughterAmount(){
		MyTypeHolder result = ctrt.getTotalSlaughterAmount();
		int resultint = result.getInteger();
		System.out.println(resultint);
		assertEquals(131984, resultint);
	}
	
	@Test
	public void testGetTotalCurrentSlaughterAmount(){
		MyTypeHolder result = ctrt.getTotalCurrentSlaughterAmount();
		int resultint = result.getInteger();
		System.out.println(resultint);
		assertEquals(0, resultint);
	}
}
