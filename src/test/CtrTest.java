package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.Controller;
import dba.DBSingleConnection;
import dba.DatabaseAccess;
import model.WorkingTeam;

public class CtrTest {
	public Controller ctr = null;
	public DatabaseAccess dba = null;
	public DBSingleConnection dbSinCon = null;
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
	}

	@Test
	public void getSpeedTest() {
		int now = 1479731288;
		int before = now - 3600;
//		int speed = ctr.getSpeed(before, now);
		
//		assertEquals(speed, 13000);
		// 13000 is calculated manually 
	}
	
	
	public void getAvgWeightTest(){
		int now = 1479731288;
		int before = now - 3600;
		
//		int testVar = ctr.getAvgWeight(before, now);
		
//		assertEquals(testVar, 1000);
		
	}
	
//	@Test
//	public void testGetSlaughterAmount() {
//		ctr.getCurrentWorkingTeam();
//		int expectedAmount = 144579;
//		int actualAmount = ctr.getTotalSlaughterAmount().getInteger();
//		System.out.println("Actual amount fetched: " + actualAmount);
//		assertEquals(actualAmount, expectedAmount);
//		
//	}
	
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
