package com_gmail_sleepy771.astorage.examples.plain_text;
import java.util.Map;
import java.util.TreeMap;

import com_gmail_sleepy771.astorage.SerializationException;
import com_gmail_sleepy771.astorage.Storable;

public class Table implements Storable {
	
	private Top top;
	private Leg leg;
	private String name;
	
	public static class Top implements Storable{
		float width, height;
		
		public Top(float w, float h){
			this.width = w;
			this.height = h;
		}
		
		public float getW(){
			return this.width;
		}
		
		public float getH(){
			return this.height;
		}
		
		@Override
		public Map<String, Object> serialize() throws SerializationException {
			TreeMap<String, Object> map = new TreeMap<String, Object>();
			map.put("width", this.width);
			map.put("height", this.height);
			return map;
		}
		
	}
	
	public static class Leg implements Storable {
		float length;
		
		public Leg(float length){
			this.length = length;
		}

		@Override
		public Map<String, Object> serialize() throws SerializationException {
			TreeMap<String, Object> map = new TreeMap<String, Object>();
			map.put("length", this.length);
			return map;
		}
	}
	
	public Table(String name, Top top, Leg leg){
		this.name = name;
		this.top = top;
		this.leg = leg;
	}
	
	@Override
	public Map<String, Object> serialize() throws SerializationException {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("name", name);
		map.put("top", top);
		map.put("leg", leg);
		return map;
	}

}
