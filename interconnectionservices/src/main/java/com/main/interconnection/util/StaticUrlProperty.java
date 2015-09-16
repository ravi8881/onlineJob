package com.main.interconnection.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticUrlProperty {
	
	static Properties prop = null;
	static InputStream input = null;
	private static final Logger logger = LoggerFactory.getLogger(StaticUrlProperty.class);
	
	public StaticUrlProperty()
	{
		logger.info("StaticUrlProperty:::"+StaticUrlProperty.class);
	}
	
	public static Properties getResourceFile() {
		try {
			prop = new Properties();
			input = StaticUrlProperty.class.getClassLoader()
					.getResourceAsStream("social.properties");
			prop.load(input);// load properties file
		} catch (Exception e) {
			logger.info("Properties getResourceFile()::"+StaticUrlProperty.class);
		}
		return prop;
	}

}
