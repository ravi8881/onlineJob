package com.main.interconnection.util;

public class ParseUtils {
	public static Integer stringToInt(String str){
		if(null!=str&&!"".equals(str)){
			return Integer.parseInt(str);
		}
		return 1;
	}
	public static String stringToInt(Integer str){
		if(null!=str){
			return String.valueOf(str);
		}
		return null;
	}
}
