package com.main.interconnection.clientBo;


/*
 * @author Name:Gaurav chugh
 * @Created Date:09/11/2014
 * @update Date:27/11/2014
 * @purpose:UserDetails
 */
public class UserDetails {
	public User users;
	public String isFriend="0";
	private String requestId="undefined";
	
		public String getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
	public User getUsers() {
		return users;
	}
	public void setUsers(User users) {
		this.users = users;
	}

	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
