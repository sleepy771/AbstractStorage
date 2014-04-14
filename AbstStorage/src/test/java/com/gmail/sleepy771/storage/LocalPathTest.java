package com.gmail.sleepy771.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LocalPathTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private LocalPath lp;
    private final String[] path = {"table1", "row_21", "var_ref_image"};

    @Before
    public void setUp() throws Exception {
	LocalPath.Builder lpb = new LocalPath.Builder();
	lpb.add(path);
	lp = lpb.build();
	lpb = null;

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAsList() {
	List<String> li = lp.asList();
	for (int k = 0; k < li.size(); k++) {
	    assertEquals(path[k], li.get(k));
	}
    }

    @Test
    public void testGet() {
	assertEquals(path[2], lp.get(2));
    }

    @Test
    public void testGetNode() {
	assertEquals(path[0], lp.getNode());
    }

    @Test
    public void testGetSubpath() {
	Path p = lp.getSubpath();
	int k = 1;
	for (String s : p) {
	    assertEquals(s, path[k]);
	    k++;
	}
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator() {
	Iterator<String> s = lp.iterator();
	assertTrue(s.hasNext());
	assertEquals(s.next(), path[0]);
	s.remove();
    }
    
    @Test
    public void testToString() {
	assertEquals("\\lp@table1.row_21.var_ref_image", lp.toString());
    }

}
