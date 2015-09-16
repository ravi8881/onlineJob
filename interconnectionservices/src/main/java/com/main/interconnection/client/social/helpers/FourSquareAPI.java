package com.main.interconnection.client.social.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.JSONFieldParser;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.ResultMeta;
import fi.foyt.foursquare.api.entities.Checkin;
import fi.foyt.foursquare.api.entities.CompleteUser;
import fi.foyt.foursquare.api.entities.UserGroup;
import fi.foyt.foursquare.api.io.DefaultIOHandler;
import fi.foyt.foursquare.api.io.IOHandler;
import fi.foyt.foursquare.api.io.Method;
import fi.foyt.foursquare.api.io.Response;

public class FourSquareAPI {

	@Value("${foursquare.apikey}")
	String fourSquareAppId;

	@Value("${foursquare.apisecret}")
	String fourSquareAppSecret;

	@Value("${foursquare.callbackurl}")
	String fourSquareCallBackUrl;

	private final String Defualt_Version = getCurrentTimeStamp();

	private static final Logger selflogger = LoggerFactory
			.getLogger(FourSquareAPI.class);
	private boolean useCallback = true;

	
	

	private boolean skipNonExistingFields = true;
	private static final String apiUrl = "https://api.foursquare.com/v2/";
	
	public FourSquareAPI() {
	
	}

	

	public Result<CompleteUser> getFourSquareUserDetails(String userID,
			String accessToken) {
		FoursquareApi foursquareApi = new FoursquareApi(fourSquareAppId,
				fourSquareAppSecret, fourSquareCallBackUrl, accessToken,
				new DefaultIOHandler());
		try {
			foursquareApi.setVersion(Defualt_Version);
			return foursquareApi.user(userID);
		} catch (FoursquareApiException e) {
			selflogger
					.info(" Error Occur to get foursquare User Details == LogTime"
							+ Calendar.getInstance().getTime());

		}
		return null;
	}

	public Result<UserGroup> getFourSquareFriends(String userID,
			String accessToken) {
		FoursquareApi foursquareApi = new FoursquareApi(fourSquareAppId,
				fourSquareAppSecret, fourSquareCallBackUrl, accessToken,
				new DefaultIOHandler());
		try {
			foursquareApi.setVersion(Defualt_Version);
			return foursquareApi.usersFriends(userID);
		} catch (FoursquareApiException e) {
			selflogger
					.info(" Error Occur to get foursquare User Details == LogTime"
							+ Calendar.getInstance().getTime());

		}
		return null;
	}

	public Result<UserGroup> getFourSquareFriendsWithPaging(String userID,
			String accessToken, int limit, int offset) {
	
		ApiRequestResponse response = null;
		UserGroup result = null;
		try {
			if (userID == null) {
				userID = "self";
			}
			try {
				response = doApiRequest(Method.GET, "users/" + userID
						+ "/friends", true,new DefaultIOHandler(), "limit", String.valueOf(limit),
						"offset", String.valueOf(offset),"oauth_token",accessToken);

				if (response.getMeta().getCode() == 200) {
					result = (UserGroup) JSONFieldParser.parseEntity(
							UserGroup.class, response.getResponse()
									.getJSONObject("friends"),
							this.skipNonExistingFields);
				}
			} catch (FoursquareApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (JSONException e) {
			e.printStackTrace();
			// throw new FoursquareApiException(e);
		}
		return new Result<UserGroup>(response.getMeta(), result);
	}

	public Result<Checkin> postOnCheckin(String userID, String accessToken) {
		FoursquareApi foursquareApi = new FoursquareApi(fourSquareAppId,
				fourSquareAppSecret, fourSquareCallBackUrl, accessToken,
				new DefaultIOHandler());
		try {
			foursquareApi.setVersion(Defualt_Version);
			return foursquareApi.checkinsAdd(null, "Test", "This is Check",
					"public", null, null, null, null);
		} catch (FoursquareApiException e) {
			selflogger
					.info(" Error Occur to get foursquare User Details == LogTime"
							+ Calendar.getInstance().getTime());

		}
		return null;
	}

	/*
	 * public Result<CompleteUser> getFourSquareFriendDetails(String userID) {
	 * FoursquareApi foursquareApi = new FoursquareApi(null,
	 * null,null,"NNUAFJUW1XAW45APPWRJL0VP5F0DUNKF2FKH3SBSPAW5ZXWZ", new
	 * DefaultIOHandler()); try { foursquareApi.setVersion(Defualt_Version);
	 * return foursquareApi.user(userID); } catch (FoursquareApiException e) {
	 * selflogger .info(" Error Occur to get foursquare User Details == LogTime"
	 * + Calendar.getInstance().getTime());
	 * 
	 * } return null; }
	 */
	/*
	 * public static void main(String[] args) { FourSquareAPI obj= new
	 * FourSquareAPI(); System.out.println("The friend details are ==== "+obj.
	 * getFourSquareFriendDetails
	 * ("91439922").getResult().getContact().getEmail()); }
	 */
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	/*public Result<UserGroup> usersFriends(String userId)
			throws FoursquareApiException {
		try {
			if (userId == null) {
				userId = "self";
			}

			ApiRequestResponse response = doApiRequest(Method.GET, "users/"
					+ userId + "/friends", true);
			UserGroup result = null;

			if (response.getMeta().getCode() == 200) {
				result = (UserGroup) JSONFieldParser.parseEntity(
						UserGroup.class,
						response.getResponse().getJSONObject("friends"),
						this.skipNonExistingFields);
			}

			return new Result<UserGroup>(response.getMeta(), result);
		} catch (JSONException e) {
			throw new FoursquareApiException(e);
		}
	}*/

	private ApiRequestResponse doApiRequest(Method method, String path,
			boolean auth,IOHandler ioHandler, Object... params) throws JSONException,
			FoursquareApiException {
		String url = getApiRequestUrl(path, auth, params);
		Response response = ioHandler.fetchData(url, method);

		if (useCallback) {
			return handleCallbackApiResponse(response);
		} else {
			return handleApiResponse(response);
		}
	}

	private String getApiRequestUrl(String path, boolean auth, Object... params)
			throws FoursquareApiException {
		StringBuilder urlBuilder = new StringBuilder(apiUrl);
		urlBuilder.append(path);
		urlBuilder.append('?');

		if (params.length > 0) {
			int paramIndex = 0;
			try {
				while (paramIndex < params.length) {
					Object value = params[paramIndex + 1];
					if (value != null) {
						urlBuilder.append(params[paramIndex]);
						urlBuilder.append('=');
						urlBuilder.append(URLEncoder.encode(value.toString(),
								"UTF-8"));
						urlBuilder.append('&');
					}

					paramIndex += 2;
				}
			} catch (UnsupportedEncodingException e) {
				throw new FoursquareApiException(e);
			}
		}

		/*if (auth) {
			urlBuilder.append("oauth_token=");
			urlBuilder.append(this.oAuthToken);
		} else {
			urlBuilder.append("client_id=");
			urlBuilder.append(this.clientId);
			urlBuilder.append("&client_secret=");
			urlBuilder.append(this.clientSecret);
		}*/

		urlBuilder.append("&v=" + Defualt_Version);

		if (useCallback) {
			urlBuilder.append("&callback=c");
		}

		return urlBuilder.toString();
	}

	private ApiRequestResponse handleCallbackApiResponse(Response response)
			throws JSONException {
		if (response.getResponseCode() == 200) {
			String responseContent = response.getResponseContent();
			String callbackPrefix = "c(";
			String callbackPostfix = ");";
			JSONObject responseObject = new JSONObject(
					responseContent.substring(callbackPrefix.length(),
							responseContent.length() - callbackPostfix.length()));

			JSONObject metaObject = responseObject.getJSONObject("meta");
			int code = metaObject.getInt("code");
			String errorType = metaObject.optString("errorType");
			String errorDetail = metaObject.optString("errorDetail");

			JSONObject responseJson = responseObject.getJSONObject("response");
			JSONArray notificationsJson = responseObject
					.optJSONArray("notifications");

			return new ApiRequestResponse(new ResultMeta(code, errorType,
					errorDetail), responseJson, notificationsJson);
		} else {
			return new ApiRequestResponse(new ResultMeta(
					response.getResponseCode(), "", response.getMessage()),
					null, null);
		}
	}

	private ApiRequestResponse handleApiResponse(Response response)
			throws JSONException {
		JSONObject responseJson = null;
		JSONArray notificationsJson = null;
		String errorDetail = null;

		if (response.getResponseCode() == 200) {
			JSONObject responseObject = new JSONObject(
					response.getResponseContent());
			responseJson = responseObject.getJSONObject("response");
			notificationsJson = responseObject.optJSONArray("notifications");
		} else {
			errorDetail = response.getMessage();
		}

		return new ApiRequestResponse(new ResultMeta(
				response.getResponseCode(), "", errorDetail), responseJson,
				notificationsJson);
	}

	
	private class ApiRequestResponse {

		/**
		 * Constructor
		 * 
		 * @param meta
		 *            status information
		 * @param response
		 *            response JSON Object
		 * @param notifications
		 *            notifications JSON Object
		 * @throws JSONException
		 *             when JSON parsing error occurs
		 */
		public ApiRequestResponse(ResultMeta meta, JSONObject response,
				JSONArray notifications) throws JSONException {
			this.meta = meta;
			this.response = response;
			this.notifications = notifications;
		}

		/**
		 * Returns response JSON Object
		 * 
		 * @return response JSON Object
		 */
		public JSONObject getResponse() {
			return response;
		}

		/**
		 * Returns notifications JSON Object
		 * 
		 * @return notifications JSON Object
		 */
		public JSONArray getNotifications() {
			return notifications;
		}

		/**
		 * Returns status information
		 * 
		 * @return status information
		 */
		public ResultMeta getMeta() {
			return meta;
		}

		private JSONObject response;
		private JSONArray notifications;
		private ResultMeta meta;
	}

}
