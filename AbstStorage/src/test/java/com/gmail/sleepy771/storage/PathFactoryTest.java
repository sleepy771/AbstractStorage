package com.gmail.sleepy771.storage;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathFactoryTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testParsePath() {
	Path p = PathFactory.parsePath("\\gp@stor1.table1.row3.var_a");
	assertEquals(GlobalPath.class, p.getClass());
	assertEquals("\\gp@stor1.table1.row3.var_a", p.toString());
	Path lp = PathFactory.parsePath("\\lp@table1.row3.var_a");
	assertEquals(LocalPath.class, lp.getClass());
	assertEquals("\\lp@table1.row3.var_a", lp.toString());
    }

}
