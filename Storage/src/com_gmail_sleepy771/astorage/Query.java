package com_gmail_sleepy771.astorage;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {
	
	public static final String REFERENCE_SERIALS = "8b09c035383797518202fe7a3b7d2abc";

	public class Interval {
		private Comparable<Object> min, max;

		public Interval(Comparable<Object> min, Comparable<Object> max) {
			setInitialPoint(min);
			setLastPoint(max);
		}

		private void setInitialPoint(Comparable<Object> min) {
			this.min = min;
		}

		private void setLastPoint(Comparable<Object> max) {
			this.max = max;
		}

		public Comparable<Object> getInitialPoint() {
			return this.min;
		}

		public Comparable<Object> getLastPoint() {
			return this.max;
		}

		public boolean contains(Comparable<Object> p) {
			if (!p.getClass().equals(min.getClass()))
				throw new IllegalArgumentException(); // Not comparable;
														// different types
			return p.compareTo(min) * p.compareTo(max) == -1;
		}
	}

	private TreeMap<String, Object> criteriaMap = new TreeMap<String, Object>();

	public Query() {
	}

	public void addRegexCriteria(String varName, String regex) {
		criteriaMap.put(varName, Pattern.compile(regex));
	}

	public boolean addEqualityCriteria(String varName, Object obj) {
		if (obj.getClass().isPrimitive() || obj.getClass().equals(String.class)) {
			criteriaMap.put(varName, obj);
			return true;
		}
		return false;
	}

	/*
	 * Este porozmyslat o koncepcii writable Objectu
	 */
	public boolean addIntervalCriteria(String varName, Comparable<Object> min,
			Comparable<Object> max) {
		if (!min.getClass().equals(max.getClass())) {
			throw new IllegalArgumentException(); // Cannot compare between two
													// different types
		}
		if (!(min.getClass().isPrimitive() || min.getClass().equals(
				String.class)))
			throw new IllegalArgumentException();
		this.criteriaMap.put(varName, new Interval(min, max));
		return false;
	}

	public Map.Entry<String, Object> poll() {
		if (this.hasCriteria())
			return this.criteriaMap.pollFirstEntry();
		return null;
	}

	public boolean hasCriteria() {
		return !this.criteriaMap.isEmpty();
	}
	
	public static boolean asStrings(ObjectData data){
		boolean strings = true;
		for(Object val : data.values()){
			strings &= val.getClass().equals(String.class);
		}
		return strings;
	}
	
	public boolean satisfies(ObjectData od){
		boolean satisfies = true;
		if(asStrings(od)){
			for(Map.Entry<String, Object> entry : criteriaMap.entrySet()){
				if (!od.containsKey(entry.getKey()) ){
					return false;
				}
				if (entry.getValue().getClass().equals(Pattern.class)) {
					Pattern p = Pattern.class.cast(entry.getValue());
					Matcher m = p.matcher(od.get(entry.getKey()).toString());
					if(!m.matches())
						return false;
				} else if(entry.getValue().getClass().equals(Interval.class)) {
					
				} else {
					
				}
			}
		} else {
			
		}
		return satisfies;
	}
}
