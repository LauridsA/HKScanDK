package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.Controller;
import dba.DatabaseAccess;

public class CtrTest {
	public Controller ctr = null;
	public DatabaseAccess dba = null;
	@Before
	public void setUp() throws Exception {
		ctr = new Controller();
		dba = new DatabaseAccess();
		
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
		int speed = ctr.getSpeed(before, now);
		
		assertEquals(speed, 13000);
		// 13000 is calculated manually 
	}
	
	
	public void getAvgWeightTest(){
		int now = 1479731288;
		int before = now - 3600;
		
		int testVar = ctr.getAvgWeight(before, now);
		
		assertEquals(testVar, 1000);
		
	}
	
	
	

}
