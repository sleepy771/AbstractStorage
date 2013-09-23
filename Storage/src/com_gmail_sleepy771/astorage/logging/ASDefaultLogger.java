package com_gmail_sleepy771.astorage.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ASDefaultLogger {
	private static FileHandler logFile;
	private static SimpleFormatter logFormater;
	
	public static void setup() throws IOException{
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.INFO);
		
		logFile = new FileHandler("AStorage.log", 256*1024, 1, true);
		logFormater = new SimpleFormatter();
		logFile.setFormatter(logFormater);
		
		logger.addHandler(logFile);
		
	}
}
