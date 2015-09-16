package com.main.interconnection.util;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class GoogleConstants {
	public static final String JSON_MIMETYPE = "application/json";

	public static final String CURRENT_USER_SESSION_KEY = "me";

	public static final String ACTIVITY_LIST = "public";

	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	public static final HttpTransport TRANSPORT = new NetHttpTransport();
	
}
