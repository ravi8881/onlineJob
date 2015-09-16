package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errorcode")
public class ErrorCode {

	
	
	public int errorcode;
	public  String error;
	
	ErrorCode(int errorcode, String error){
		this.errorcode = errorcode;
		this.error = error;
	}

	public static String MENDATORY_FIELD_MISSING_400 = "Mandatory field missing";// 400
	public static String RESOURCE_NOT_AVILABLE_404 = "Invalid Uri";// 404
	public static String ERROR_WHILE_PROCESSING_405 = "Error while processing";// 405
	
	public static String TOKEN_MISSING_99 = "Security/Authentication token missing or invalid";// 99
	public static String INVALID_API_KEY_100 = "Api key invalid";// 100
	public static String MISSING_PARAMETERS_101 = "Missing parameters";// 101
	public static String AUTHENTICATION_FAIL_102 = "Authentication failed";// 102
	
	public static String EMAIL_NOT_EXISTS_103 = "EmailId Not Exists";// 103
	public static String MISSING_API_KEY_104 = "Api key missing";// 104
	public static String INVALID_PARAMETER_105 = "Invalid Parameter";// 105	
	public static String INVALID_ACTOVATION_CODE_106 = "Invalid activation code or user Id";// 106	
	public static String STATE_NOT_FOUND_107 = "No user state found";// 107	
	public static String CITY_NOT_FOUND_108 = "No user city found";// 108
	public static String CATERGORY_NOT_FOUND_109 = "No category found";// 109
	public static String EMAIL_ALREADY_EXISTS_110 = "EmailId Already Exists";// 110	
	public static String NO_SATATE_EXITS_111 = "No State Exist with this state id";// 111
	public static String NO_CITY_EXITS_112 = "No city Exist with this city id";// 112
	private static final String LOGIN_ATTEMPT_EXCCEED = "Login attempt exceeded.So Access Denied for 30 minutes";
	
	public static String ERROR_WHILE_PROCESSING_1001 = "Error occured while processing parameter";// 1001	
	public static String CUSTOM_ERROR_1002 = "Error occured while processing parameter";// 1002
	
	
	public static String UNABLE_TO_REGISTER_API_3001 = "Unable to register Api key";// 3001
	public static String UNABLE_TO_REGISTER_USER_3002 = "Unable to register user";// 3002	
	public static String EMAIL_ALL_READY_REGISTER_3003 = "Email already register";// 3003
	public static String USER_NOT_AGREE_3004 = "User has not agreed to terms and conditions";// 3004	
	public static String INVALID_USER_NAME_PASSWORD_3005 = "Invalid Username Or Password";// 3005
	//changed due to bug reported BG2
	//public static String INVALID_USER_NAME_PASSWORD_3005 = "Invalid user or password";
	public static String USER_INACTIVE_3006 = "User is inactive";// 3006	
	public static String INVALID_USERID_TOKEN_EXPIRE_3007 = "Invalid user id / Token has expired ";// 3007
	public static String INVALID_USERID_3008 = "Invalid user id / Token has expired ";// 3008
	public static String USER_NOT_LOGGED_IN_3009 = "User not logged in ";// 3009
	public static String INVALID_EMAILID_TEMP_TOKEN_3010 = "Invalid EmailId / Temp Token ";// 3010
	
	public static String UNABLE_TO_UPDATE_PASSWORD_3011 = "Unable to update password ";// 3011
	
	public static String ACTIVATION_URL_NOT_EXITS_4001 = "Activation url not exists";// 4001
	
	public static String INVALID_REQUEST_CODE_5000 = "Invalid Request Id";// 5000
	
	public static String INVALID_UPDATE_ID_5001 = "Invalid Update Id";// 5001
	
	public static String SOCIAL_USER_NOT_FOUND_5002 = "User Not Found";// 5002
	
	public static String NO_SOCIAL_FRIENDS_FOUND_5003 = "None of your friends are on Waspit";// 5003
	
	public static String NOT_CONNECTED_TO_SOCIAL_5004 = "User Disconnected";// 5004
	 
	public static  String NOT_CONNECTED_TO_FB = "User Account not connected to facebook";
	public static  String FOURSQUARE_API_EXCEPTION_5006 = "Foursquare API Exception";
	
	public static  String INVALID_OLD_PASSWORD_6001 = "Invalid Old password";
	

	public static  String NO_RECIEVED_REQUEST_6002 = "No pending requests";
	

	public static  String INVALID_ACCESS_TOKEN_7001 = "Access Token Missing/Invalid";
	
	public static  String INVALID_ACCESS_SECRET_7002 = "Access Token Secret Missing/Invalid";
	
	
	public static  String STATUS_NOT_POSTED_7003 = "Status has not been posted";
	

	public static  ErrorCode getCustomeError(int errorcode){	
		
		switch (errorcode) {
		case 400:return new ErrorCode(errorcode, MENDATORY_FIELD_MISSING_400);
		case 404:return new ErrorCode(errorcode, RESOURCE_NOT_AVILABLE_404);
		case 405:return new ErrorCode(errorcode, ERROR_WHILE_PROCESSING_405);
		case 99:return new ErrorCode(errorcode, TOKEN_MISSING_99);
		case 100:return new ErrorCode(errorcode, INVALID_API_KEY_100);
		case 101:return new ErrorCode(errorcode, MISSING_PARAMETERS_101);
		case 102:return new ErrorCode(errorcode, AUTHENTICATION_FAIL_102);
		case 103:return new ErrorCode(errorcode, EMAIL_NOT_EXISTS_103);
		case 104:return new ErrorCode(errorcode, MISSING_API_KEY_104);
		case 105:return new ErrorCode(errorcode, INVALID_PARAMETER_105);
		case 106:return new ErrorCode(errorcode, INVALID_ACTOVATION_CODE_106);
		case 107:return new ErrorCode(errorcode, STATE_NOT_FOUND_107);
		case 108:return new ErrorCode(errorcode, CITY_NOT_FOUND_108);
		case 109:return new ErrorCode(errorcode, CATERGORY_NOT_FOUND_109);
		case 110:return new ErrorCode(errorcode, EMAIL_ALREADY_EXISTS_110);
		case 111:return new ErrorCode(errorcode, NO_SATATE_EXITS_111);
		case 112:return new ErrorCode(errorcode, NO_CITY_EXITS_112);
		case 113:return new ErrorCode(errorcode, LOGIN_ATTEMPT_EXCCEED);
		
		
		
		
		
		case 1001:return new ErrorCode(errorcode, ERROR_WHILE_PROCESSING_1001);
		case 1002:return new ErrorCode(errorcode, CUSTOM_ERROR_1002);
		
		case 3001:return new ErrorCode(errorcode, UNABLE_TO_REGISTER_API_3001);
		case 3002:return new ErrorCode(errorcode, UNABLE_TO_REGISTER_USER_3002);
		case 3003:return new ErrorCode(errorcode, EMAIL_ALL_READY_REGISTER_3003);
		case 3004:return new ErrorCode(errorcode, USER_NOT_AGREE_3004);
		case 3005:return new ErrorCode(errorcode, INVALID_USER_NAME_PASSWORD_3005);
		case 3006:return new ErrorCode(errorcode, USER_INACTIVE_3006);
		case 3007:return new ErrorCode(errorcode, INVALID_USERID_TOKEN_EXPIRE_3007);
		case 3008:return new ErrorCode(errorcode, INVALID_USERID_3008);
		case 3009:return new ErrorCode(errorcode, USER_NOT_LOGGED_IN_3009);
		case 3010:return new ErrorCode(errorcode, INVALID_EMAILID_TEMP_TOKEN_3010);
		case 3011:return new ErrorCode(errorcode, UNABLE_TO_UPDATE_PASSWORD_3011);
		
		case 4001:return new ErrorCode(errorcode, ACTIVATION_URL_NOT_EXITS_4001);
		
		case 5000:return new ErrorCode(errorcode, INVALID_REQUEST_CODE_5000);
		
		case 5001:return new ErrorCode(errorcode, INVALID_UPDATE_ID_5001);
		

		//Added for Social Error...
		case 5002:return new ErrorCode(errorcode, SOCIAL_USER_NOT_FOUND_5002);
		case 5003:return new ErrorCode(errorcode, NO_SOCIAL_FRIENDS_FOUND_5003);
		case 5004:return new ErrorCode(errorcode, NOT_CONNECTED_TO_SOCIAL_5004);
		case 5006:return new ErrorCode(errorcode, FOURSQUARE_API_EXCEPTION_5006);
		

		case 6000:return new ErrorCode(errorcode, NOT_CONNECTED_TO_FB);
		
		case 6001:return new ErrorCode(errorcode, INVALID_OLD_PASSWORD_6001);

		
		case 6002:return new ErrorCode(errorcode, NO_RECIEVED_REQUEST_6002);


		
		case 7001: return new ErrorCode(errorcode, INVALID_ACCESS_TOKEN_7001);
		
		case 7002: return new ErrorCode(errorcode, INVALID_ACCESS_SECRET_7002);
		
		case 7003: return new ErrorCode(errorcode, STATUS_NOT_POSTED_7003);
		

		default:
			return new ErrorCode(errorcode, "No Message Avilable");
		}
	}
}
