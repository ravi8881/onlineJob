package com.main.interconnection.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GoogleRestClient {
	public static String httpAuthConnection(String urlName,String authToken)throws Exception{
		String output=null;
		StringBuilder outputData = new StringBuilder();
		try {
			URL url = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization","Bearer "+authToken);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
	 
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			//System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				//System.out.println("HTTPutil:httpGETConnection >"+output);
				outputData.append(output);
			}
			conn.disconnect();
		  } catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception();
		  } catch (IOException e) {
			e.printStackTrace();
			throw new Exception();
		  }
	 return outputData.toString();
	}
	
	public static String httpGETConnection(String urlName)throws Exception{
		String output=null;
		StringBuilder outputData = new StringBuilder();
		try {
			URL url = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
	 
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
	 
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			//System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				//System.out.println("HTTPutil:httpGETConnection >"+output);
				outputData.append(output);
			}
			conn.disconnect();
		  } catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception();
		  } catch (IOException e) {
			e.printStackTrace();
			throw new Exception();
		  }
	 return outputData.toString();
	}

}
