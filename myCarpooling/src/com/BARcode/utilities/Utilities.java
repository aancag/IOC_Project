package com.BARcode.utilities;

public class Utilities {

	public static boolean fieldsValid(String... params) {
		
		for (String string : params) {
			if (string.equals("")) {
				return false;
			}
		}
		
		return true;
	}
	
}
