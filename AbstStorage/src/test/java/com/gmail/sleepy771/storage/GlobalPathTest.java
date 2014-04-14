package com.gmail.sleepy771.storage;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GlobalPathTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {


    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private GlobalPath gp;
    private final String[] path = { "table1", "ref_to_table2", "lastRow",
	    "var_z" };
    private final String storage = "stor1";

    @Before
    public void setUp() throws Exception {
	GlobalPath.Builder gpb = new GlobalPath.Builder(storage);
	gpb.add(path);
	gp = gpb.build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGet() {
	System.out.println(gp);
	assertEquals(gp.get(0), path[0]);
	assertEquals(gp.get(1), path[1]);
    }

    @Test
    public void testGetNode() {
	assertEquals(gp.getNode(), storage);
    }

    @Test
    public void testGetStorageName() {
	assertEquals(gp.getStorageName(), storage);
    }
    
    @Test
    public void testToString() {
	assertEquals("\\gp@stor1.table1.ref_to_table2.lastRow.var_z", gp.toString());
    }

}
