package com_gmail_sleepy771.astorage;

public class Serials {
	private static long currentSerialNumber = 0L;
	
	public synchronized static long generateSerial(){
		return currentSerialNumber++;
	}

	public synchronized static void setSerialNumber(long serial) {
		currentSerialNumber = serial;
	}
}
