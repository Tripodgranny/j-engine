package com.tripod.main;

import java.time.LocalDate;

public class Log {
	
	private final static int versionNumber = 1;
	private final static LocalDate date = LocalDate.now();
	
	// LOG MESSAGES
	public static final String VERSION = "JEngine Version " + versionNumber + " : " + date;
	
	// LOG BY CODE
	public static void code (String code) {
		System.out.println(code);
	}

}
