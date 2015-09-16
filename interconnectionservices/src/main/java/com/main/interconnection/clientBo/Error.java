package com.main.interconnection.clientBo;



import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class Error {

	 Integer errorcode;
	 String message;
	 
	public Error() {

	}

	public Error(Integer errorcode) {
		this.errorcode = errorcode;
	}

	public Error(Integer errorcode, String message) {
		this.errorcode = errorcode;
		this.message = message;
	}

	public Integer getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Generate Error Message
	 */

	public static String INVALID_API_KEY = "Api key invalid";// 100
	public static String MISSING_PARAMETERS = "Missing parameters";// 101
	public static String AUTHENTICATION_FAIL = "Authentication failed";// 102
	public static String EMAIL_ALREADY_EXISTS = "EmailId Already Exists";// 102
	public static String EMAIL_NOT_EXISTS = "EmailId Not Exists";// 103
	public static String MISSING_API_KEY = "Api key missing";// 104
	public static String INVALID_PARAMETER = "Invalid Parameter";// 105	
	public static String INVALID_ACTOVATION_CODE = "Invalid activation code or user Id";// 106	
	public static String STATE_NOT_FOUND = "No user state found";// 107	
	public static String CITY_NOT_FOUND = "No user city found";// 108
	public static String CATERGORY_NOT_FOUND = "No category found";// 109
	
	public static String ERROR_WHILE_PROCESSING = "Error occure while processing Parameter";// 1001
	
	
	public static String UNABLE_TO_REGISTER_API = "Unable to register Api key";// 3001
	public static String UNABLE_TO_REGISTER_USER = "Unable to register user";// 3002	
	public static String EMAIL_ALL_READY_REGISTER = "Email all ready register";// 3003
	public static String USER_NOT_AGREE = "User not agree term and condition";// 3004
	
	public static String INVALID_USER_NAME_PASSWORD = "Invalid user name password";// 3005
	
	public static String USER_INACTIVE = "User is inactive";// 3006
	

	/**
	 * Generates Error message
	 * 
	 * @param code
	 * @return
	 */
	public Error generateError(int errorcode) {

		Error error = new Error(errorcode);

		if (errorcode == 100) {
			error.setMessage(INVALID_API_KEY);
		} else if (errorcode == 101) {
			error.setMessage(MISSING_PARAMETERS);
		} else if (errorcode == 102) {
			error.setMessage(AUTHENTICATION_FAIL);
		}else if (errorcode == 103) {
			error.setMessage(EMAIL_NOT_EXISTS);
		}else if (errorcode == 104) {
			error.setMessage(MISSING_API_KEY);
		}else if (errorcode == 105) {
			error.setMessage(INVALID_PARAMETER);
		}else if (errorcode == 106) {
			error.setMessage(INVALID_ACTOVATION_CODE);
		}else if (errorcode == 107) {
			error.setMessage(STATE_NOT_FOUND);
		}else if (errorcode == 108) {
			error.setMessage(CITY_NOT_FOUND);
		}else if (errorcode == 109) {
			error.setMessage(CATERGORY_NOT_FOUND);
		}
		else if (errorcode == 1001) {
			error.setMessage(ERROR_WHILE_PROCESSING);
		}
		else if (errorcode == 2001) {
			error.setMessage(EMAIL_ALREADY_EXISTS);
		}else if (errorcode == 3001) {
			error.setMessage(UNABLE_TO_REGISTER_API);
		}else if (errorcode == 3002) {
			error.setMessage(UNABLE_TO_REGISTER_USER);
		}else if (errorcode == 3003) {
			error.setMessage(EMAIL_ALL_READY_REGISTER);
		}else if (errorcode == 3004) {
			error.setMessage(USER_NOT_AGREE);
		}else if (errorcode == 3005) {
			error.setMessage(INVALID_USER_NAME_PASSWORD);
		}else if (errorcode == 3006) {
			error.setMessage(USER_INACTIVE);
		}

		return error;
	}

}
