package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "messagecode")
public class Message {

	public  int messagecode;
	public  String message;
	
	Message(int messagecode, String message){
		this.messagecode = messagecode;
		this.message = message;
	}
	
	
	public static String NO_MESSAGE = "No Message ";// 0
	public static String VENUE_ADDED_SUCESFULLY = "Venue Added Sucesfully ";// 1
	
	public static String ACTIVATION_LINK_SENT_2 = "Activation link email sent sucessfully ";// 2
	
	
	public static String SECURITY_CODE_EXISTS_3 = "SecurityCode exists ";// 3
	public static String SECURITY_CODE_NOT_EXISTS_4 = "Invalid Security Code";// 4
	
	public static String PASSWORD_RESET_SUCESSFULLY_5 = "Password Reset Sucessfully ";// 5
	
	public static String USER_LGOUT_6 = "User logout Sucessfully ";// 6
	
	public static String USER_DELETED_7="User Deleted Sucessfully";//7
	
	public static String DELETED_8="Deleted Successfully";//7
	
	public static String PASSWORD_CHANGED_9 = "Password Changed Successfully";// 9
	
	public static String FRIEND_REMOVED_10="Friend Removed Successfully";//10
	 
	public static  Message getCustomeMessages(int messgecode){	
		switch (messgecode) {
		
		case 0: return new Message(messgecode , NO_MESSAGE);
		case 1: return new Message(messgecode , VENUE_ADDED_SUCESFULLY);
		case 2: return new Message(messgecode , ACTIVATION_LINK_SENT_2);
		case 3: return new Message(messgecode , SECURITY_CODE_EXISTS_3);
		case 4: return new Message(messgecode , SECURITY_CODE_NOT_EXISTS_4);
		case 5: return new Message(messgecode , PASSWORD_RESET_SUCESSFULLY_5);
		case 6: return new Message(messgecode , USER_LGOUT_6);
		case 7: return new Message(messgecode , USER_DELETED_7);
		case 8: return new Message(messgecode , DELETED_8);
		case 9: return new Message(messgecode , PASSWORD_CHANGED_9);
		case 10: return new Message(messgecode , FRIEND_REMOVED_10);
		default:
			return new Message(messgecode, "No Message Available");
		}
	}
	
	// System.out.println(getCustomeMessages(1).message+""+getCustomeMessages(1).messagecode);
	// System.out.println(getCustomeMessages(2).message+""+getCustomeMessages(2).messagecode);
	
}
