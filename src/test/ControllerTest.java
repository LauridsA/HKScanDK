package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Controller;

public class ControllerTest {
	Controller ctr = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Controller ctr = new Controller();
	}

	@After
	public void tearDown() throws Exception {
		Controller ctr = null;
	}

	@Test
	public void testGetValue() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testGetOrganic() {
		assertTrue(ctr.getOrganic().getBool());
	}

	@Test
	public void testGetRefreshRate() {
		fail("Not yet implemented");
	}

}
