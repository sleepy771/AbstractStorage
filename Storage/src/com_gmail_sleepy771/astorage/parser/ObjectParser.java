package com_gmail_sleepy771.astorage.parser;

import java.text.ParseException;

public abstract class ObjectParser {
	public abstract Object parseValue(String value) throws ParseException;
	public abstract String getRegex();
	
	public boolean matches(String value){
		return value.matches(getRegex());
	}
	
	public static class BooleanParser extends ObjectParser{

		@Override
		public Object parseValue(String value) throws ParseException {
			if(!value.matches(getRegex())){
				throw new ParseException(value+" value is not Boolean type", 0);
			}
			return Boolean.valueOf(value.toLowerCase());
		}

		@Override
		public String getRegex() {
			return "([tT][rR][uU][eE])|([fF][aA][lL][sS][eE])";
		}
		
	}
	
	public static class CharacterParser extends ObjectParser {

		@Override
		public Object parseValue(String value) throws ParseException {
			if(!value.matches(getRegex())){
				throw new ParseException(value+ " value is not Character type", 0);
			}
			return value.charAt(0);
		}

		@Override
		public String getRegex() {
			return "[^\\d]";
		}
		
	}
	
	public static class DoubleParser extends ObjectParser {

		@Override
		public Object parseValue(String value) throws ParseException {
			if(!value.matches(getRegex())){
				throw new ParseException(value+ " value is not Double type", 0);
			}
			return Double.valueOf(value);
		}

		@Override
		public String getRegex() {
			return "[+-]?\\d*(\\.\\d+)?([eE][+-]?\\d+)?";
		}
		
	}
	
	public static class LongParser extends ObjectParser {

		@Override
		public Object parseValue(String value) throws ParseException {
			if (!value.matches(getRegex())){
				throw new ParseException(value+ " value is not Long type", 0);
			}
			return Long.valueOf(value);
		}

		@Override
		public String getRegex() {
			return "[+-]?\\d+";
		}
		
	}
}
