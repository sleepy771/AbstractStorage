package com.gmail.sleepy771.datastructures;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gmail.sleepy771.exceptions.NotBuildableException;
import com.gmail.sleepy771.storage.LocalPath;

public class DataBuilderImplTest {
    private DataBuilder db;
    private class MockClass{
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
	this.db = new DataBuilderImpl();
	db.setClass(MockClass.class);
	db.setName("mockClass");
	db.put("a", 13).put("b", 14);
	DataBuilder inner = new DataBuilderImpl("innerObject", MockClass.class);
	inner.put("c", 15);
	db.put("innerObject", inner);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetString() {
	assertEquals(13, db.get("a"));
	assertEquals(14, db.get("b"));
	assertEquals(DataBuilderImpl.class, db.get("innerObject").getClass());
    }

    @Test
    public void testGetPath() {
	LocalPath.Builder lpb = new LocalPath.Builder();
	lpb.add(new String[]{"innerObject", "c"});
	LocalPath lp = lpb.build();
	assertEquals(15, db.get(lp));
    }

    @Test
    public void testGetName() {
	assertEquals("mockClass", db.getName());
    }

    @Test
    public void testGetByClass() {
	Map<String, Object> testInt = db.getByClass(Integer.class);
	for(Entry<String, Object> entry : testInt.entrySet()) {
	    assertEquals(Integer.class, entry.getValue().getClass());
	}
    }

    @Test
    public void testGetRepresentedClass() {
	assertEquals(MockClass.class, db.getRepresentedClass());
    }

    @Test
    public void testPut() {
	String varName = "someString";
	String value = "Hello Schroddy!";
	db.put(varName, value);
	assertEquals(value, db.get(varName));
    }

    @Test
    public void testPutAllMapOfStringObject() {
	//TODO fukken map parser is must have parser that translates array of strings to map {"hello : schroddy", "good bye : Denis"}
	Map<String, Object> dataMap = new HashMap<String, Object>();
	dataMap.put("somevalue", "val");
	dataMap.put("approxPi", 3.1415927);
	dataMap.put("approxE", 2.718281828459);
	db.putAll(dataMap);
	assertEquals("val", db.get("somevalue"));
	assertEquals(3.1415927, db.get("approxPi"));
	assertEquals(2.718281828459, db.get("approxE"));
    }

    @Test
    public void testRemove() {
	db.put("str", "str_val");
	assertEquals("str_val", db.get("str"));
	db.remove("str");
	assertNull(db.get("str"));
    }

    @Test
    public void testRemoveAll() {
	db.put("a1", "val1").put("b2", "val2").put("c3", "val3");
	assertEquals("val1", db.get("a1"));
	assertEquals("val2", db.get("b2"));
	assertEquals("val3", db.get("c3"));
	List<String> remList = Arrays.asList("a1", "b2", "c3");
	db.removeAll(remList);
	assertNull(db.get("a1"));
	assertNull(db.get("b2"));
	assertNull(db.get("c3"));
	
    }

    @Test
    public void testSetName() {
	db.setName("otherName");
	assertEquals("otherName", db.getName());
	db.setName("mockClass");
    }

    @Test
    public void testSetClass() {
	class MockClass2{
	}
	db.setClass(MockClass2.class);
	assertEquals(MockClass2.class, db.getRepresentedClass());
	db.setClass(MockClass.class);
    }

}
