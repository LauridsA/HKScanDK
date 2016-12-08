package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
		ctrt.getCurrentWorkingTeam(times().get(0));
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
	/*
	@Test
	public void SpeedTestFail() {
		MyTypeHolder speed = ctrt.getSpeed();
		int speedint = speed.getInteger();
		assertEquals(speedint, 13001);
	}
	*/
	
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
	
	/**
	 * Either logic or SQL is broken. Returns remaining minutes until done with day?
	 */
	@Test
	public void testExpectedFinish(){
		
		MyTypeHolder testRes = ctrt.expectedFinish();
		int testResInt = testRes.getInteger();
		System.out.println("expectedfinish: " + testResInt);
		assertEquals(9, (long)testResInt);
	}
	
	/**
	 * SQL might be broken. seems to take teamid rather than teamtimetableid.
	 */
	@Test
	public void testExpectedPerHour(){
		
		MyTypeHolder testRes = ctrt.expectedPerHour();
		int testResInt = testRes.getInteger();
		System.out.println("expectedperhour: " + testResInt);
		assertEquals(1481198400000L, testResInt);
	}
	
	/**
	 * SQL seems to be broken.
	 */
	@Test
	public void testDayExpected(){
		MyTypeHolder testRes = ctrt.dayExpected();
		int testResInt = testRes.getInteger();
		System.out.println("dayexpected: " + testResInt);
		assertEquals(848, testResInt);
	}
	
	/**
	 * appears to work
	 */
	@Test
	public void testGetTotalSlaughterAmount(){
		MyTypeHolder result = ctrt.getTotalSlaughterAmount();
		int resultInt = result.getInteger();
		System.out.println("total SA : " + resultInt);
		assertEquals(131984, resultInt);
		ctrt.getCurrentWorkingTeam(times().get(1));
		MyTypeHolder testRes1 = ctrt.dayExpected();
		int testResInt1 = testRes1.getInteger();
		assertEquals(123137, testResInt1);
		ctrt.getCurrentWorkingTeam(times().get(2));
		MyTypeHolder testRes2 = ctrt.dayExpected();
		int testResInt2 = testRes2.getInteger();
		assertEquals(128513, testResInt2);
	}
	
	/**
	 * appears to work
	 */
	@Test
	public void testGetTotalCurrentSlaughterAmount(){
		MyTypeHolder result = ctrt.getTotalCurrentSlaughterAmount();
		int resultInt = result.getInteger();
		System.out.println("total current SA : " + resultInt);
		assertEquals(131136, resultInt);
		ctrt.getCurrentWorkingTeam(times().get(1));
		MyTypeHolder testRes1 = ctrt.dayExpected();
		int testResInt1 = testRes1.getInteger();
		assertEquals(122576, testResInt1);
		ctrt.getCurrentWorkingTeam(times().get(2));
		MyTypeHolder testRes2 = ctrt.dayExpected();
		int testResInt2 = testRes2.getInteger();
		assertEquals(127624, testResInt2);
	}
	
	public ArrayList<Long> times(){
		ArrayList<Long> times = new ArrayList<>();
		times.add(1480305900000L);
		times.add(1483475550000L);
		times.add(1484020990000L);
		return times;
	}
}
