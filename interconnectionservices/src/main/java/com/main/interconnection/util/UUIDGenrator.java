package com.main.interconnection.util;

import java.util.Random;
import java.util.UUID;

public class UUIDGenrator { 
	
	static final  Random random = new Random();
	
    public static String getUniqueId(){
        return UUID.randomUUID().toString().toUpperCase();                
    }
    
    public static String generateCaptchaCode() {
		int length = 7 + (Math.abs(random.nextInt()) % 3);

		StringBuffer captchaStringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int baseCharNumber = Math.abs(random.nextInt()) % 62;
			int charNumber = 0;
			if (baseCharNumber < 26) {
				charNumber = 65 + baseCharNumber;
			}
			else if (baseCharNumber < 52){
				charNumber = 97 + (baseCharNumber - 26);
			}
			else {
				charNumber = 48 + (baseCharNumber - 52);
			}
			captchaStringBuffer.append((char)charNumber);
		}
		return captchaStringBuffer.toString().trim();
	}
    
}