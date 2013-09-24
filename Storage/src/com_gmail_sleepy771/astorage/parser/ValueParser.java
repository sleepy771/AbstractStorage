package com_gmail_sleepy771.astorage.parser;

import java.text.ParseException;
import java.util.HashSet;
import java.util.logging.Logger;

public class ValueParser {
	public static final HashSet<ObjectParser> PARSERS = new HashSet<ObjectParser>();
	private static final Logger LOGGER = Logger.getLogger(ValueParser.class.getCanonicalName());
	
	static{
		PARSERS.add(new ObjectParser.BooleanParser());
		PARSERS.add(new ObjectParser.CharacterParser());
		PARSERS.add(new ObjectParser.DoubleParser());
		PARSERS.add(new ObjectParser.LongParser());
	}
	
	/*
	 * Maybe parse value should throw ParseException
	 */
	public static Object parseValue(String value) {
		Object out = value;
		for(ObjectParser parser : PARSERS){
			if(parser.matches(value)){
				try{
					out = parser.parseValue(value);
				}
				catch(ParseException pe){
					LOGGER.severe("Unknown value type");
					LOGGER.throwing(parser.getClass().getCanonicalName(), "parseValue()", pe);
				}
			}
		}
		return out;
	}
	
	public static void main(String[] arg){
		String t = "a";
		Object o = parseValue(t);
		System.out.println(o);
		System.out.println(o.getClass().getCanonicalName());
	}
}
