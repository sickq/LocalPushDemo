package com.fsq.utils;

public class Utils {
	
	public static boolean isNullOrEmpty(String paramString) {
		return (paramString == null) || (paramString.length() == 0) || (paramString.trim().length() == 0);
	}
}
