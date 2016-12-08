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
		assertEquals(232, (long)testResInt);
	}
	
	/**
	 * SQL might be broken. seems to take teamid rather than teamtimetableid. only accepts 1, no other number (???)
	 */
	@Test
	public void testExpectedPerHour(){
		
		MyTypeHolder testRes = ctrt.expectedPerHour();
		int testResInt = testRes.getInteger();
		System.out.println("expectedperhour: " + testResInt);
		assertEquals(1481198400000L, testResInt);
	}
	
	/**
	 * SQL seems to be broken. only accepts 1, no other number (????)
	 */
	@Test
	public void testDayExpected(){
		MyTypeHolder testRes = ctrt.dayExpected(2);
		int testResInt = testRes.getInteger();
		System.out.println("dayexpected: " + testResInt);
		assertEquals(848, testResInt);
	}
	
	/**
	 * appears to work but has teamid instead of teamtimetable???
	 */
	@Test
	public void testGetTotalSlaughterAmount(){
		MyTypeHolder result = ctrt.getTotalSlaughterAmount(2);
		int resultInt = result.getInteger();
		System.out.println("total SA : " + resultInt);
		assertEquals(131984, resultInt);
		
		
		ctrt.getCurrentWorkingTeam(times().get(1));
		MyTypeHolder testRes1 = ctrt.getTotalSlaughterAmount(54);
		int testResInt1 = testRes1.getInteger();
		assertEquals(136596, testResInt1);
		
		ctrt.getCurrentWorkingTeam(times().get(2));
		MyTypeHolder testRes2 = ctrt.getTotalSlaughterAmount(64);
		int testResInt2 = testRes2.getInteger();
		assertEquals(128513, testResInt2);
		
	}
	
	/**
	 * appears to work but has teamid instead of teamtimetable???
	 */
	@Test
	public void testGetTotalCurrentSlaughterAmount(){
		MyTypeHolder result = ctrt.getTotalCurrentSlaughterAmount(2);
		int resultInt = result.getInteger();
		System.out.println("total current SA : " + resultInt);
		assertEquals(131136, resultInt);
		
		MyTypeHolder testRes1 = ctrt.getTotalCurrentSlaughterAmount(54);
		int testResInt1 = testRes1.getInteger();
		assertEquals(135680, testResInt1);
		
		MyTypeHolder testRes2 = ctrt.getTotalCurrentSlaughterAmount(64);
		int testResInt2 = testRes2.getInteger();
		assertEquals(127624, testResInt2);
		
	}
	
	public ArrayList<Long> times(){
		ArrayList<Long> times = new ArrayList<>();
		times.add(1480305900000L); //2
		times.add(1483475550000L); //54
		times.add(1483993910000L); //64
		return times;
	}
	
	/**
	 * passed!
	 */
	@Test
	public void testGetNoStopDay(){
		
		MyTypeHolder result = ctrt.getNoStopDay(1480305900000L);
		int resultInt = result.getInteger();
		System.out.println("WHAT : " +resultInt);
		assertEquals(4, resultInt);
		
		MyTypeHolder testRes1 = ctrt.getNoStopDay(1483475550000L);
		int testResInt1 = testRes1.getInteger();
		assertEquals(0, testResInt1);
		
		MyTypeHolder testRes2 = ctrt.getNoStopDay(1483993910000L);
		int testResInt2 = testRes2.getInteger();
		assertEquals(0, testResInt2);
	}
	
	/**
	 * passed!
	 */
	@Test
	public void TestGetCurrentSlaughterAmountDay(){
		MyTypeHolder result = ctrt.getCurrentSlaughterAmountDay(1480305900000L);
		int resultInt = result.getInteger();
		System.out.println("WHAT : " +resultInt);
		assertEquals(63796, resultInt);
		
		MyTypeHolder testRes1 = ctrt.getCurrentSlaughterAmountNight(1483475550000L);
		int testResInt1 = testRes1.getInteger();
		assertEquals(56056, testResInt1);
		
		MyTypeHolder testRes2 = ctrt.getCurrentSlaughterAmountNight(1483993910000L);
		int testResInt2 = testRes2.getInteger();
		assertEquals(95612, testResInt2);
		
	}
}
