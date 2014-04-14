package com.gmail.sleepy771.datastructures;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gmail.sleepy771.storage.LocalPath;

public class DataIntTest {
    private DataInt d;
    private class Table1{
    }
    private class Row{
    }
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
	DataBuilder db = new DataBuilderImpl("Table_1", Table1.class);
	db.put("rows", 2);
	DataBuilder dbRow1 = new DataBuilderImpl("Row_1", Row.class);
	DataBuilder dbRow2 = new DataBuilderImpl("Row_2", Row.class);
	dbRow1.put("a", 13).put("b", 2.25);
	dbRow2.put("a", 1).put("b", 2.24);
	db.put("Row_1", dbRow1).put("Row_2", dbRow2);
	d = db.build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetString() {
	assertEquals(2, d.get("rows"));
    }

    @Test
    public void testGetPath() {
	LocalPath lp = new LocalPath.Builder().add(new String[]{"Row_1", "b"}).build();
	LocalPath lp2 = new LocalPath.Builder().add("Row_2").add("a").build();
	assertEquals(2.25, d.get(lp));
	assertEquals(1, d.get(lp2));
    }

    @Test
    public void testGetName() {
	assertEquals("Table_1", d.getName());
    }

    @Test
    public void testGetByClass() {
	Map<String, Object> map = d.getByClass(Integer.class);
	assertEquals(2, map.get("rows"));
	assertNull(map.get("Row_1"));
    }

    @Test
    public void testGetRepresentedClass() {
	assertEquals(Table1.class, d.getRepresentedClass());
    }

    @Test
    public void testIterator() {
	fail("Not yet implemented");
    }

}
